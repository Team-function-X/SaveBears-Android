package com.junction.savebears.local.room.dao

import androidx.room.Query
import com.junction.savebears.local.room.Challenge
import kotlinx.coroutines.flow.Flow

interface ChallengeDao {

    @Query("SELECT * FROM challenges")
    fun getAllChallenges(): Flow<List<Challenge>>

    @Query("DELETE FROM challenges")
    fun deleteAllChallenges(): Flow<Unit>
}