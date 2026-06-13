package com.example.budgetbuddy1.database

import androidx.room.*

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategory(category: Category)

    @Query("SELECT * FROM categories WHERE userId = :userId")
    suspend fun getCategoriesForUser(userId: Int): List<Category>

    @Query("SELECT * FROM categories WHERE name = :name AND userId = :userId LIMIT 1")
    suspend fun getCategoryByName(name: String, userId: Int): Category?
}
