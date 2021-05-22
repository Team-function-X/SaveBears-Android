package com.junction.savebears.view

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import com.junction.savebears.R
import com.junction.savebears.base.BaseActivity
import com.junction.savebears.component.Status
import com.junction.savebears.component.UiState
import com.junction.savebears.component.ext.loadUri
import com.junction.savebears.databinding.ActivityRegisterChallengeBinding
import com.junction.savebears.remote.model.GlacierResponse
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import kotlin.random.Random

class RegisterChallengeActivity : BaseActivity() {

    private lateinit var binding: ActivityRegisterChallengeBinding
    private val uiState = MutableLiveData<UiState<GlacierResponse>>()
    private lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterChallengeBinding.inflate(layoutInflater)
        setOnClicks()
    }

    private fun setOnClicks() {
        binding.selectImageView.setOnClickListener {
            CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setActivityTitle("Add Image")
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setCropMenuCropButtonTitle("Submit")
                .setRequestedSize(1920, 1080)
                .start(this)
        }

        binding.uploadImageButton.setOnClickListener {
            uploadChallengeImage(image = imageUri)
        }
    }

    /**
     * uploadImageButton 를 눌렀을 때 실행
     * - 서버에 챌린지 수행 인증 이미지를 전송
     */
    private fun uploadChallengeImage(image: Uri) {
        // TODO 저장소에서 uri로 get 하기
        //  1. contentProvider에 cursor로 접근해 사진 정보를 가져오기
        //  2. 사진 백엔드에서 요구하는 형식에 맞게 변환 후 Request
        //   - 성공 -> 해당 점수 반영(기획 필요)
        //   - 실패 -> 다시 시도 로직(기획 필요)
        //   - 통신 중 -> 기획 필요
    }

    /**
     * 사용자가 이미지 선택을 완료하면 실행됨
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // TODO 사진을 특정 naming의 파일로 저장하기
        //  형식 : 앱이름 + yyyy_MM_dd_hhmmss 추천

        // 업로드를 위한 사진이 선택 및 편집되면 Uri 형태로 결과가 반환됨
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)

            if (resultCode == Activity.RESULT_OK) {
                // 갤러리에서 선택된 이미지의 Uri 가 담겨있음
                val resultUri = result.uri
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, resultUri)

                // 갤러리에서 삭제되는 상황을 대비하여 앱 전용 로컬 디렉토리에 따로 저장하고, Uri 반환받음
                val imageUri = bitmapToFile(bitmap!!) // Uri

                // 선택된 이미지를 ImageView 에 적용함

                binding.selectImageView.loadUri(imageUri) {
                    placeholder(R.mipmap.ic_launcher)
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Timber.d("이미지 선택 및 편집 오류")
            }
        }
    }

    /**
     * Bitmap 이미지를 Local 에 저장하고, URI 를 반환함
     **/
    private fun bitmapToFile(bitmap: Bitmap): Uri {
        val wrapper = ContextWrapper(this)
        val randomNumber = Random.nextInt(0, 1000000000).toString()
        // Bitmap 파일 저장을 위한 File 객체
        var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
        file = File(file, "item_${randomNumber}.jpg")
        try {
            // Bitmap 파일을 JPEG 형태로 압축해서 출력
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
            Timber.d(e)
        }
        return Uri.parse(file.absolutePath)
    }

    private fun getChallengeUris() {

    }

    override fun observeUiResult() {
        uiState.observe(this) {
            when (it.status) {
                Status.SUCCESS -> { // 성공했을 때
                }
                Status.LOADING -> { // 로딩중일 때
                }
                Status.ERROR -> { // 실패했을 때
                    Timber.e(it.message)
                }
                Status.EMPTY -> { // 데이터가 비었을 때
                }
            }
        }
    }
}