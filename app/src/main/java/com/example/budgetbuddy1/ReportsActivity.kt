package com.example.budgetbuddy1

import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.budgetbuddy1.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReportsActivity : AppCompatActivity() {

    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reports)

        userId = intent.getIntExtra("USER_ID", -1)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        loadReportData()
    }

    private fun loadReportData() {
        val db = AppDatabase.getDatabase(this)

        lifecycleScope.launch {
            // Fetch User Budget
            val userBudget = withContext(Dispatchers.IO) {
                db.budgetDao().getBudgetForUser(userId)
            }
            val budgetMax = userBudget?.maxSpend ?: 0.0

            // Fetch User Expenses
            val allExpenses = withContext(Dispatchers.IO) {
                db.expenseDao().getExpensesForUser(userId)
            }

            var totalAmount = 0.0
            val categoryMap = mutableMapOf<String, Double>()

            for (expense in allExpenses) {
                totalAmount += expense.amount
                categoryMap[expense.category] = categoryMap.getOrDefault(expense.category, 0.0) + expense.amount
            }

            findViewById<TextView>(R.id.tvTotalExpenses).text = "Total Expenses: R${String.format("%.2f", totalAmount)}"
            
            val remaining = budgetMax - totalAmount
            val tvRemaining = findViewById<TextView>(R.id.tvBudgetRemaining)
            tvRemaining.text = "Budget Remaining: R${String.format("%.2f", remaining)}"
            
            if (remaining < 0) {
                tvRemaining.setTextColor(Color.RED)
            } else {
                tvRemaining.setTextColor(Color.parseColor("#10A173"))
            }

            // Update Chart
            if (totalAmount > 0) {
                val chartDataList = mutableListOf<DonutChartView.ChartData>()
                val colors = listOf("#10A173", "#6236FF", "#F5A623", "#00A8E1", "#FF5252")
                var colorIndex = 0

                categoryMap.forEach { (_, amount) ->
                    val percentage = ((amount / totalAmount) * 100).toFloat()
                    val color = Color.parseColor(colors[colorIndex % colors.size])
                    chartDataList.add(DonutChartView.ChartData(percentage, color))
                    colorIndex++
                }

                val chart = findViewById<DonutChartView>(R.id.donutChartDynamic)
                chart?.setData(chartDataList)
            }
        }
    }
}