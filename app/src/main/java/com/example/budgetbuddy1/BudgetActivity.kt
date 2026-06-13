package com.example.budgetbuddy1

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.budgetbuddy1.database.AppDatabase
import com.example.budgetbuddy1.database.Budget
import com.google.android.material.bottomnavigation.BottomNavigationView

class BudgetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget)

        val minInput = findViewById<EditText>(R.id.etMin)
        val maxInput = findViewById<EditText>(R.id.etMax)
        val saveBtn = findViewById<Button>(R.id.btnSaveBudget)
        val result = findViewById<TextView>(R.id.tvBudgetStatus)

        val userId = intent.getIntExtra("USER_ID", -1)
        val db = AppDatabase.getDatabase(this)

        // Load saved values from database
        val savedBudget = db.budgetDao().getBudgetForUser(userId)
        if (savedBudget != null) {
            minInput.setText(savedBudget.minSpend.toString())
            maxInput.setText(savedBudget.maxSpend.toString())
            result.text = "Current Budget:\nMin: R${savedBudget.minSpend}\nMax: R${savedBudget.maxSpend}"
        }

        saveBtn.setOnClickListener {
            val min = minInput.text.toString().toDoubleOrNull()
            val max = maxInput.text.toString().toDoubleOrNull()

            if (min == null || max == null) {
                Toast.makeText(this, "Enter valid values", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (min > max) {
                Toast.makeText(this, "Min cannot be greater than Max", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // SAVE TO DATABASE
            val budget = Budget(userId = userId, minSpend = min, maxSpend = max)
            db.budgetDao().saveBudget(budget)

            result.text = "Budget Saved:\nMin: R$min\nMax: R$max"
            Toast.makeText(this, "Budget isolation active", Toast.LENGTH_SHORT).show()
        }

        setupBottomNavigation(userId)
    }

    private fun setupBottomNavigation(userId: Int) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.selectedItemId = R.id.nav_goals
        
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, DashboardActivity::class.java).apply { 
                        putExtra("USER_ID", userId)
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    })
                    true
                }
                R.id.nav_expenses -> {
                    startActivity(Intent(this, ViewExpensesActivity::class.java).apply { putExtra("USER_ID", userId) })
                    true
                }
                R.id.nav_add -> {
                    startActivity(Intent(this, AddExpenseActivity::class.java).apply { putExtra("USER_ID", userId) })
                    true
                }
                R.id.nav_reports -> {
                    startActivity(Intent(this, ReportsActivity::class.java).apply { putExtra("USER_ID", userId) })
                    true
                }
                R.id.nav_goals -> true
                else -> false
            }
        }
    }
}
