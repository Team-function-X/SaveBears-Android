package com.junction.savebears.local.room.dao

import androidx.room.*
import com.junction.savebears.local.room.Challenge
import kotlinx.coroutines.flow.Flow

@Dao
interface ChallengeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(roomObject: Challenge)

    // 모든 첼린지
    @Transaction
    @Query("SELECT * FROM challenges")
    fun getAllChallenges(): Flow<List<Challenge>>

    // 날짜에 해당하는 첼린지
    @Transaction
    @Query("SELECT * FROM challenges WHERE id = :id")
    fun getChallengesById(id: Int): Flow<List<Challenge>>

    @Query("DELETE FROM challenges")
    fun deleteAllChallenges()
}