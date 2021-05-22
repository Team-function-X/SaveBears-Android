package com.junction.savebears.local.room

import androidx.room.TypeConverter
import java.util.*

class RoomConverters {

    @TypeConverter
    fun stringToList(string: String): List<String> = provideStringTypeAdapter().fromJson(string).orEmpty()

    @TypeConverter
    fun listToString(list: List<String>): String = provideStringTypeAdapter().toJson(list)

    @TypeConverter
    fun fromTimestamp(value: Long): Date = Date(value)

    @TypeConverter
    fun dateToTimestamp(date: Date): Long = date.time
}