package com.junction.savebears.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.junction.savebears.local.room.dao.ChallengeDao

@Database(
    entities =
    [
        ChallengeItem::class,
    ],
    version = 1,
    exportSchema = true
)
abstract class LocalDataBase : RoomDatabase() {

    abstract fun challengeDao(): ChallengeDao
}