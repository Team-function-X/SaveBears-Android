package com.junction.savebears.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageUrlResponse(
    @field:Json(name = "uploadUrl")
    val uploadUrl: String
)
