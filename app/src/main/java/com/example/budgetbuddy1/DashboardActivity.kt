package com.example.budgetbuddy1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val tvWelcome = findViewById<TextView>(R.id.tvWelcome)
        val username = intent.getStringExtra("USERNAME") ?: "User"
        val userId = intent.getIntExtra("USER_ID", -1)
        tvWelcome.text = getString(R.string.welcome_user, username)

        val btnAddExpense = findViewById<Button>(R.id.btnAddExpense)
        val btnViewExpenses = findViewById<Button>(R.id.btnViewExpenses)
        val btnBudget = findViewById<Button>(R.id.btnBudget)
        val btnReports = findViewById<Button>(R.id.btnReports)
        val btnRewards = findViewById<Button>(R.id.btnRewards)

        btnAddExpense.setOnClickListener {
            val intent = Intent(this, AddExpenseActivity::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
        }

        btnViewExpenses.setOnClickListener {
            val intent = Intent(this, ViewExpensesActivity::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
        }

        btnBudget.setOnClickListener {
            val intent = Intent(this, BudgetActivity::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
        }

        btnReports.setOnClickListener {
            val intent = Intent(this, ReportsActivity::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
        }

        btnRewards.setOnClickListener {
            val intent = Intent(this, SpinActivity::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
        }
    }
}