package com.junction.savebears.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.junction.savebears.local.room.dao.ChallengeDao

@Database(
    entities =
    [
        Challenge::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(RoomConverters::class)
abstract class LocalDataBase : RoomDatabase() {

    abstract fun challengeDao(): ChallengeDao
}