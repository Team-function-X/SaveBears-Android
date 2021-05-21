package com.junction.savebears.local.room

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

private fun provideMoshi(): Moshi = Moshi.Builder().build()

fun provideStringTypeAdapter(): JsonAdapter<List<String>> =
    provideMoshi().adapter(Types.newParameterizedType(List::class.java, String::class.java))