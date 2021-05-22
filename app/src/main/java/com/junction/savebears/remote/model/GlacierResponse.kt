package com.junction.savebears.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GlacierResponse(
    @field:Json(name = "Glacier")
    val status: String,
    @field:Json(name = "Changed Ratio")
    val value: Int
)
