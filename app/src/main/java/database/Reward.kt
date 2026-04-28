package com.example.budgetbuddy1.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rewards")
data class Reward(
    @PrimaryKey
    val userId: Int,
    val totalBalance: Double,
    val lastSpinDate: String // Format: "yyyy-MM-dd"
)