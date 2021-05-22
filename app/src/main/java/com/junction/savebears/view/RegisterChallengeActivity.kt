package com.junction.savebears.view

import android.app.Activity
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.amplifyframework.core.Amplify
import com.google.android.material.snackbar.Snackbar
import com.junction.savebears.BuildConfig
import com.junction.savebears.R
import com.junction.savebears.base.BaseActivity
import com.junction.savebears.component.Status
import com.junction.savebears.component.UiState
import com.junction.savebears.component.ext.bitmapToFile
import com.junction.savebears.component.ext.drawableToByteArray
import com.junction.savebears.component.ext.loadUri
import com.junction.savebears.component.ext.toSimpleString
import com.junction.savebears.databinding.ActivityRegisterChallengeBinding
import com.junction.savebears.local.room.Challenge
import com.junction.savebears.remote.model.ChallengeResponse
import com.junction.savebears.remote.model.GlacierResponse
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.w3c.dom.Comment
import timber.log.Timber
import java.io.File
import java.util.*

class RegisterChallengeActivity : BaseActivity() {

    private lateinit var binding: ActivityRegisterChallengeBinding
    private val dao get() = roomDatabase.challengeDao()
    private var imageUri: Uri? = null
    var date = SimpleDateFormat("yy.MM.dd", Locale.getDefault()).format(Date())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterChallengeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setOnClicks()
    }

    private fun setOnClicks() {
        binding.addImageButton.setOnClickListener {
            CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setActivityTitle("Add Image")
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setCropMenuCropButtonTitle("Submit")
                .setRequestedSize(1000, 1000)
                .start(this)
        }

        // Image 수정 버튼
        binding.editImageButton.setOnClickListener {
            CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setActivityTitle("Edit Image")
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setCropMenuCropButtonTitle("Submit")
                .setRequestedSize(1000, 1000)
                .start(this)
        }

        // Image 업로드 버튼 (API 호출)
        binding.uploadImageButton.setOnClickListener {
            if ((imageUri != null)) {
                uploadChallengeImage(image = imageUri!!)
            } else {
                Snackbar.make(binding.root, "Please Upload Image!", Snackbar.LENGTH_LONG)
            }
        }

    }

    /**
     * uploadImageButton 를 눌렀을 때 실행
     * - 서버에 챌린지 수행 인증 이미지를 전송
     */
    private fun uploadChallengeImage(image: Uri) {
        var comment: String = binding.challengeCommentEditText.text.toString()

        // TODO 저장소에서 uri로 get 하기
        //  1. contentProvider에 cursor로 접근해 사진 정보를 가져오기
        //  2. 사진 백엔드에서 요구하는 형식에 맞게 변환 후 Request
        //   - 성공 -> 해당 점수 반영(기획 필요)
        //   - 실패 -> 다시 시도 로직(기획 필요)
        //   - 통신 중 -> 기획 필요

        Timber.d(image.path)
        Timber.d(image.lastPathSegment)
        uploadFileToS3(image, comment)

    }

    /**
     * Amazon S3 스토리지에 이미지 업로드
     * - 업로드 성공을 콜백으로 감지하고, Object Detection API 호출
     */
    fun uploadFileToS3(image: Uri, comment: String) {
        val file = File(image.path!!)
        val fileName = image.lastPathSegment.toString()

        Timber.d("Progress In Upload File To S3 ...")
        Amplify.Storage.uploadFile(fileName, file,
            // onSuccess
            {
                Timber.i("Successfully uploaded: ${it.key}")
                lifecycleScope.launch {
                    flow {
                        emit(challengeApi.getChallengeResult(imageName = it.key))
                    }
                        .flowOn(Dispatchers.IO)
                        .catch {
                            Timber.e("Error")
                            Timber.e(it)
                        }
                        .collect {
                            Timber.d(it.point.toString())
                            resultDialog(point = it.point, image = image, comment = comment)
                        }
                }
            },
            // onFailure
            { Timber.i("Upload failed$it") }
        )
    }

    private fun resultDialog(point: Int, image: Uri, comment: String) {
        val builder = AlertDialog.Builder(this)

        if (point >= 1) {
            builder.apply {
                this.setMessage("Challenge success! Would you like to record your success?")
                this.setNegativeButton("NO") { _, _ ->
                    finish()
                }
                this.setPositiveButton("YES") { _, _ ->
                    // Challenge 수행 내용을 Room DB 에 저장
                    insertChallenge(image, comment)
                    finish()
                }
            }
            builder.show()
        } else {
            builder.apply {
                this.setMessage("Challenge failed. Please take a picture again or upload another picture.")
                this.setNegativeButton("NO") { _, _ -> }
                this.setPositiveButton("YES") { _, _ -> }
            }
            builder.show()
        }

    }

    private fun insertChallenge(image: Uri, comment: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            if (BuildConfig.DEBUG) {
                val challengeEntity = Challenge(
                    missionCompleteDate = date,
                    imageSignature = contentResolver.openInputStream(image)?.readBytes()!!,
                    imageStrUri = image.toString(),
                    comment = comment
                )
                dao.insert(challengeEntity)
            }
        }
    }

    /**
     * 사용자가 이미지 선택을 완료하면 실행됨
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // TODO 사진을 특정 naming 의 파일로 저장하기
        //  형식 : 앱이름 + yyyy_MM_dd_hhmmss 추천

        // 업로드를 위한 사진이 선택 및 편집되면 Uri 형태로 결과가 반환됨
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)

            if (resultCode == Activity.RESULT_OK) {
                // 갤러리에서 선택된 이미지의 Uri 가 담겨있음
                val resultUri = result.uri
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, resultUri)

                // 갤러리에서 삭제되는 상황을 대비하여 앱 전용 로컬 디렉토리에 따로 저장하고, Uri 반환받음
                val storedImageUri = bitmapToFile(bitmap!!) // Uri

                binding.addImageButton.visibility = View.GONE
                binding.uploadedImageCardView.visibility = View.VISIBLE

                // 선택된 이미지를 ImageView 에 적용함
                binding.uploadImageView.loadUri(resultUri) {
                    placeholder(R.mipmap.ic_launcher)
                }
                imageUri = resultUri
                Timber.d(storedImageUri.toString())
                Timber.d(resultUri.toString())

                // 현재 날짜를 dateText 에 적용
                binding.dateText.text = date

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Timber.d("이미지 선택 및 편집 오류")
            }
        }
    }

    private fun getChallengeUris() {

    }
}