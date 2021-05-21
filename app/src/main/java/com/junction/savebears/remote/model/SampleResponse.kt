package com.junction.savebears.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class SampleResponse(// TODO Edit Samples
    @Json(name = "sample")
    val sample: String
)
