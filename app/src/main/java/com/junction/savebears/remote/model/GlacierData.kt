package com.junction.savebears.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class GlacierData(// TODO Edit Samples
    @Json(name = "change")
    val change: String
)
