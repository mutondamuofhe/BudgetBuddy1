package com.example.budgetbuddy1.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Room Database class for the application.
 * Defines the entities included in the database and the version number.
 */
@Database(entities = [User::class, Expense::class, Budget::class, Reward::class], version = 7)
abstract class AppDatabase : RoomDatabase() {

    // DAOs (Data Access Objects) for each table
    abstract fun userDao(): UserDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun budgetDao(): BudgetDao
    abstract fun rewardDao(): RewardDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Singleton pattern to ensure only one instance of the database exists at a time.
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "budgetbuddy1_database"
                )
                    // Allows destructive migration (wipes data when schema changes) - useful for prototypes
                    .fallbackToDestructiveMigration()
                    // Allows queries on the main thread for simplicity in this prototype
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}