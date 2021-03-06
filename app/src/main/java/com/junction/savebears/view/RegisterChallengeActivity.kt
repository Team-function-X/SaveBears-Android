package com.junction.savebears.view

import android.app.Activity
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.amplifyframework.core.Amplify
import com.google.android.material.snackbar.Snackbar
import com.junction.savebears.R
import com.junction.savebears.base.BaseActivity
import com.junction.savebears.component.ext.bitmapToFile
import com.junction.savebears.component.ext.loadUri
import com.junction.savebears.databinding.ActivityRegisterChallengeBinding
import com.junction.savebears.local.room.Challenge
import com.junction.savebears.view.MainActivity.Companion.RESULT_KEY
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import java.io.File
import java.util.*

@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class RegisterChallengeActivity : BaseActivity() {

    private lateinit var binding: ActivityRegisterChallengeBinding
    private val dao get() = roomDatabase.challengeDao()
    private var imageUri: Uri? = null
    var date: String = SimpleDateFormat("yy.MM.dd", Locale.getDefault()).format(Date())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterChallengeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setOnClicks()
    }

    private fun setResult(result: MainActivity.Result) {
        setResult(Activity.RESULT_OK,
            Intent()
                .apply { putExtras(bundleOf(RESULT_KEY to result)) })
        finish()
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

        // Image ?????? ??????
        binding.editImageButton.setOnClickListener {
            CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setActivityTitle("Edit Image")
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setCropMenuCropButtonTitle("Submit")
                .setRequestedSize(1000, 1000)
                .start(this)
        }

        // Image ????????? ?????? (API ??????)
        binding.uploadImageButton.setOnClickListener {
            binding.challengeRegisterProgress.visibility = View.VISIBLE
            if ((imageUri != null)) {
                uploadChallengeImage(image = imageUri!!)
            } else {
                Snackbar.make(binding.root, "Please Upload Image!", Snackbar.LENGTH_LONG)
            }
        }
    }

    /**
     * uploadImageButton ??? ????????? ??? ??????
     * - ????????? ????????? ?????? ?????? ???????????? ??????
     */
    private fun uploadChallengeImage(image: Uri) {
        val comment: String = binding.challengeCommentEditText.text.toString()
        Timber.d(image.path)
        Timber.d(image.lastPathSegment)
        uploadFileToS3(image, comment)

    }

    /**
     * Amazon S3 ??????????????? ????????? ?????????
     * - ????????? ????????? ???????????? ????????????, Object Detection API ??????
     */
    fun uploadFileToS3(image: Uri, comment: String) {
        val file = File(image.path!!)
        val fileName = image.lastPathSegment.toString()

        Timber.d("Progress In Upload File To S3 ...")
        Amplify.Storage.uploadFile(fileName, file,
            // onSuccess
            {
                Thread.sleep(2000)
                Timber.i("Successfully uploaded: ${it.key}")
                lifecycleScope.launch {
                    flow { emit(challengeApi.getChallengeResult(imageName = it.key)) }
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

    /**
     * Amazon S3 ??????????????? ???????????? ????????????, Challenge API ?????? ????????? ??????????????? ???
     * ?????? ?????? (??????) ??? ?????? ?????????????????? ???????????? ??????
     */
    private fun resultDialog(point: Int, image: Uri, comment: String) {
        binding.challengeRegisterProgress.visibility = View.GONE
        val builder = AlertDialog.Builder(this)

        if (point >= 1) {  // ????????? ????????? ???????????? ???
            builder.apply {
                this.setMessage("Challenge success! Would you like to record your success?")
                this.setNegativeButton("NO") { _, _ ->
                    setResult(MainActivity.Result.Success)
                }
                this.setPositiveButton("YES") { _, _ ->
                    // Challenge ?????? ????????? Room DB ??? ??????
                    insertChallenge(image, comment)
                    setResult(MainActivity.Result.Success)
                }
            }
            builder.show()
        } else {  // ????????? ????????? ???????????? ???
            builder.apply {
                this.setMessage("Challenge failed. Please take a picture again or upload another picture.")
                this.setNegativeButton("NO") { _, _ -> setResult(MainActivity.Result.Fail) }
                this.setPositiveButton("YES") { _, _ -> setResult(MainActivity.Result.Fail) }
            }
            builder.show()
        }

    }

    private fun insertChallenge(image: Uri, comment: String) {
        lifecycleScope.launch(Dispatchers.IO) {
                val challengeEntity = Challenge(
                    missionCompleteDate = date,
                    imageSignature = contentResolver.openInputStream(image)?.readBytes()!!,
                    imageStrUri = image.toString(),
                    comment = comment
                )
                dao.insert(challengeEntity)
        }
    }

    /**
     * ???????????? ????????? ????????? ???????????? ?????????
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // ???????????? ?????? ????????? ?????? ??? ???????????? Uri ????????? ????????? ?????????
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)

            if (resultCode == Activity.RESULT_OK) {
                // ??????????????? ????????? ???????????? Uri ??? ????????????
                val resultUri = result.uri
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, resultUri)

                // ??????????????? ???????????? ????????? ???????????? ??? ?????? ?????? ??????????????? ?????? ????????????, Uri ????????????
                val storedImageUri = bitmapToFile(bitmap!!) // Uri

                binding.addImageButton.visibility = View.GONE
                binding.uploadedImageCardView.visibility = View.VISIBLE

                // ????????? ???????????? ImageView ??? ?????????
                binding.uploadImageView.loadUri(resultUri) {
                    placeholder(R.mipmap.ic_launcher)
                }
                imageUri = resultUri
                Timber.d(storedImageUri.toString())
                Timber.d(resultUri.toString())

                // ?????? ????????? dateText ??? ??????
                binding.dateText.text = date

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Timber.d("????????? ?????? ??? ?????? ??????")
            }
        }
    }

    private fun getChallengeUris() {

    }
}