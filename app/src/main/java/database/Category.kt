package com.example.budgetbuddy1.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val userId: Int // Linked to user so each user has their own categories
)
