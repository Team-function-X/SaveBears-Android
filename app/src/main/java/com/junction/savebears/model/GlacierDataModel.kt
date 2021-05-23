package com.junction.savebears.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GlacierDataModel(
    @field:Json(name = "aaaa")
    val totalChanges: Int,
    @field:Json(name = "bbbb")
    val glacierList: List<Glacier>
)

@JsonClass(generateAdapter = true)
data class Glacier(
    @field:Json(name = "aaaa")
    val imageUrl: String,
    @field:Json(name = "bbbb")
    val date: String
)
