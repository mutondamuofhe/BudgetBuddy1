package com.example.budgetbuddy1.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rewards")
data class Reward(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val amount: Double,
    val spinDate: String // Format: "yyyy-MM-dd"
)