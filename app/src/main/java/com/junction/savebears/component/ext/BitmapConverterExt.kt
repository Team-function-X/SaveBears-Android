package com.junction.savebears.component.ext

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream

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
    val bitmap = (drawable as BitmapDrawable).bitmap
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)

    return stream.toByteArray()
}