package com.example.budgetbuddy1.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budgets")
data class Budget(
    @PrimaryKey
    val userId: Int, // ONE BUDGET PER USER
    val minSpend: Double,
    val maxSpend: Double
)