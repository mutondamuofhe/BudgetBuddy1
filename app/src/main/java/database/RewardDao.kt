package com.example.budgetbuddy1.database

import androidx.room.*

@Dao
interface RewardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveReward(reward: Reward)

    @Query("SELECT * FROM rewards WHERE userId = :userId")
    suspend fun getAllRewardsForUser(userId: Int): List<Reward>

    @Query("SELECT * FROM rewards WHERE userId = :userId ORDER BY id DESC LIMIT 1")
    suspend fun getLatestRewardForUser(userId: Int): Reward?
}