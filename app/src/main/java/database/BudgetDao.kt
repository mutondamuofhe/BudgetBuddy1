package com.example.budgetbuddy1.database

import androidx.room.*

@Dao
interface BudgetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveBudget(budget: Budget)

    @Query("SELECT * FROM budgets WHERE userId = :userId LIMIT 1")
    fun getBudgetForUser(userId: Int): Budget?
}