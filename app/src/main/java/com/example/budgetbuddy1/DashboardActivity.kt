package com.example.budgetbuddy1

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.budgetbuddy1.database.AppDatabase
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val username = intent.getStringExtra("USERNAME") ?: "User"
        val userId = intent.getIntExtra("USER_ID", -1)
        
        findViewById<TextView>(R.id.tvWelcome).text = username

        val db = AppDatabase.getDatabase(this)

        // Load Financial Overview and User Data
        lifecycleScope.launch {
            // 0. Fetch User Name from DB (Robust way to keep name updated)
            val user = db.userDao().getUserById(userId)
            user?.let {
                findViewById<TextView>(R.id.tvWelcome).text = it.username
            }

            // 1. Reward Balance
            val reward = db.rewardDao().getRewardForUser(userId)
            val balance = reward?.totalBalance ?: 0.0
            findViewById<TextView>(R.id.tvSummaryBalance).text = formatCurrency(balance)

            // 2. Total Expenses
            val expenses = db.expenseDao().getExpensesForUser(userId)
            val totalSpent = expenses.sumOf { it.amount }
            findViewById<TextView>(R.id.tvSummaryExpenses).text = formatCurrency(totalSpent)

            // 3. Budget Goal (Max Spend)
            val budget = db.budgetDao().getBudgetForUser(userId)
            val maxGoal = budget?.maxSpend ?: 0.0
            findViewById<TextView>(R.id.tvSummaryBudget).text = formatCurrency(maxGoal)
        }

        setupQuickActions(userId, username)
        setupBottomNavigation(userId, username)
    }

    private fun formatCurrency(amount: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale("en", "ZA")) // South Africa (R)
        return format.format(amount)
    }

    private fun setupQuickActions(userId: Int, username: String) {
        findViewById<LinearLayout>(R.id.btnQuickAdd).setOnClickListener {
            startActivity(Intent(this, AddExpenseActivity::class.java).apply { putExtra("USER_ID", userId) })
        }
        findViewById<LinearLayout>(R.id.btnQuickView).setOnClickListener {
            startActivity(Intent(this, ViewExpensesActivity::class.java).apply { putExtra("USER_ID", userId) })
        }
        findViewById<LinearLayout>(R.id.btnQuickReports).setOnClickListener {
            startActivity(Intent(this, ReportsActivity::class.java).apply { putExtra("USER_ID", userId) })
        }
        findViewById<LinearLayout>(R.id.btnQuickBudget).setOnClickListener {
            startActivity(Intent(this, BudgetActivity::class.java).apply { putExtra("USER_ID", userId) })
        }
        findViewById<LinearLayout>(R.id.btnQuickRewards).setOnClickListener {
            startActivity(Intent(this, SpinActivity::class.java).apply { putExtra("USER_ID", userId) })
        }
        findViewById<LinearLayout>(R.id.btnQuickProfile).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java).apply { putExtra("USER_ID", userId) })
        }
    }

    private fun setupBottomNavigation(userId: Int, username: String) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.selectedItemId = R.id.nav_home
        
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_expenses -> {
                    startActivity(Intent(this, ViewExpensesActivity::class.java).apply { putExtra("USER_ID", userId) })
                    false
                }
                R.id.nav_add -> {
                    startActivity(Intent(this, AddExpenseActivity::class.java).apply { putExtra("USER_ID", userId) })
                    false
                }
                R.id.nav_reports -> {
                    startActivity(Intent(this, ReportsActivity::class.java).apply { putExtra("USER_ID", userId) })
                    false
                }
                R.id.nav_goals -> {
                    startActivity(Intent(this, BudgetActivity::class.java).apply { putExtra("USER_ID", userId) })
                    false
                }
                else -> false
            }
        }
    }
}
