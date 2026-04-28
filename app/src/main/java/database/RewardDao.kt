package com.example.budgetbuddy1.database

import androidx.room.*

@Dao
interface RewardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveReward(reward: Reward)

    @Query("SELECT * FROM rewards WHERE userId = :userId LIMIT 1")
    fun getRewardForUser(userId: Int): Reward?
}