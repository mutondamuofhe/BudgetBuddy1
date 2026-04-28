package com.example.budgetbuddy1

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.budgetbuddy1.database.AppDatabase
import com.example.budgetbuddy1.database.Budget

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
    }
}