package com.junction.savebears.model

data class GlacierDataModel(
    val totalChanges: Int,
    val glacierList: List<Glacier>
)

data class Glacier(
    val imageUrl: String,
    val date: String
)
