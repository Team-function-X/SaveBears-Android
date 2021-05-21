package com.junction.savebears.local.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.junction.savebears.local.RoomObject

@Entity(
    tableName = "challenges"
)
data class ChallengeItem(
    @PrimaryKey(autoGenerate = true)                     var id:                      Int                = 1,
    @ColumnInfo(name = "image_uri")                      val imageUri:                List<String>,
    @ColumnInfo(name = "comment")                        val comment:                 String,
) : RoomObject