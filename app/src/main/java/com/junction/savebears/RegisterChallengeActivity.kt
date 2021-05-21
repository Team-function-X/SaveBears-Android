package com.junction.savebears

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import com.bumptech.glide.Glide
import com.junction.savebears.databinding.ActivityRegisterChallengeBinding
import com.junction.savebears.remote.retrofit.ApiModule
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import kotlin.random.Random

class RegisterChallengeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterChallengeBinding
    private val saveBearsApi
        get() = ApiModule.saveBearsApi()

    private lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_challenge)

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
    private fun uploadChallengeImage(image: Uri){

    }

    /**
     * 사용자가 이미지 선택을 완료하면 실행됨
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

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
                Glide.with(this)
                    .load(imageUri)
                    .into(binding.selectImageView)

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
}