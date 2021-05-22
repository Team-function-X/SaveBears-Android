package com.junction.savebears.local.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.junction.savebears.local.RoomObject
import java.io.Serializable
import java.util.*

@Entity(
    tableName = "challenges"
)
data class Challenge(
    @PrimaryKey(autoGenerate = true)                                                       var id:                      Int                  = 1  ,
    @ColumnInfo(name = "mission_complete_date")                                            val missionCompleteDate:     String                    ,
    @ColumnInfo(name = "image_signature", typeAffinity = ColumnInfo.BLOB)                  val imageSignature:          ByteArray                 ,
    @ColumnInfo(name = "image_str_uri")                                                    val imageStrUri:             String                    ,
    @ColumnInfo(name = "comment")                                                          val comment:                 String                    ,
) : RoomObject, Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Challenge

        if (id != other.id) return false
        if (missionCompleteDate != other.missionCompleteDate) return false
        if (!imageSignature.contentEquals(other.imageSignature)) return false
        if (imageStrUri != other.imageStrUri) return false
        if (comment != other.comment) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + missionCompleteDate.hashCode()
        result = 31 * result + imageSignature.contentHashCode()
        result = 31 * result + imageStrUri.hashCode()
        result = 31 * result + comment.hashCode()
        return result
    }
}