package com.junction.savebears.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GlacierResponse(// TODO Edit Samples
    @field:Json(name = "change")
    val change: String
)
