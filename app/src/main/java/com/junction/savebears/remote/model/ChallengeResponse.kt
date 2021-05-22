package com.junction.savebears.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChallengeResponse(
    @field:Json(name = "point")
    val point: Int
)
