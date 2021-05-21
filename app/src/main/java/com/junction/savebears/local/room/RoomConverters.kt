package com.junction.savebears.local.room

import androidx.room.TypeConverter

class TypeConverters {

    @TypeConverter
    fun stringToList(string: String): List<String> = provideStringTypeAdapter().fromJson(string).orEmpty()

    @TypeConverter
    fun listToString(list: List<String>): String = provideStringTypeAdapter().toJson(list)
}