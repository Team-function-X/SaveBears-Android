package com.junction.savebears.local.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.junction.savebears.local.RoomObject
import java.util.*

@Entity(
    tableName = "challenges"
)
data class Challenge(
    @PrimaryKey(autoGenerate = true)                          var id:                      Int                  = 1  ,
    @ColumnInfo(name = "mission_complete_date")               val missionCompleteDate:     Date                      ,
    @ColumnInfo(name = "image_signature")                     val imageSignature:          String                    ,
    @ColumnInfo(name = "image_str_uri")                       val imageStrUri:             String                    ,
    @ColumnInfo(name = "comment")                             val comment:                 String                    ,
) : RoomObject