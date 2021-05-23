package com.junction.savebears.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GlacierResponse(
    @field:Json(name = "19841993")
    val data1984: Glacier,
    @field:Json(name = "19942003")
    val data1994: Glacier,
    @field:Json(name = "20042013")
    val data2004: Glacier,
    @field:Json(name = "20142020")
    val data2014: Glacier
)

@JsonClass(generateAdapter = true)
data class Glacier(
    @field:Json(name = "amount")
    val changeAmount: String,
    @field:Json(name = "image_url")
    val imageUrl: String,
    @field:Json(name = "date")
    var date: String?
)

