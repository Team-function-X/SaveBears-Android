package com.junction.savebears.component.ext

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

fun ImageView.loadUri(
    uri: Uri?,
    block: RequestOptions.() -> RequestOptions
) {
    val option = RequestOptions()
    option.block()

    Glide.with(context)
        .asBitmap()
        .load(uri)
        .apply(option)
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                setImageBitmap(resource)
            }

            override fun onLoadCleared(placeholder: Drawable?) {}
        })
}