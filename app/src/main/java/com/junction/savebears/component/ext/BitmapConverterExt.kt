package com.junction.savebears.component.ext

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.core.content.ContextCompat
import timber.log.Timber
import java.io.*
import kotlin.random.Random

fun ByteArray.toBitmap(): Bitmap? {
    return try {
        BitmapFactory.decodeByteArray(this, 0, this.count())
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun Context.drawableToByteArray(resource: Int): ByteArray {
    val drawable = ContextCompat.getDrawable(this, resource) ?: byteArrayOf()
    if(drawable !is BitmapDrawable){
        Timber.d("is not BitmapDrawable")
        return byteArrayOf()
    }

    val bitmap = drawable.bitmap
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)

    return stream.toByteArray()
}

fun Context.bitmapToFile(bitmap: Bitmap): Uri {
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