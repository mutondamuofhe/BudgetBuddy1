package com.example.budgetbuddy1

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.budgetbuddy1.database.AppDatabase
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReportsActivity : AppCompatActivity() {

    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reports)

        userId = intent.getIntExtra("USER_ID", -1)
        loadReportData()
        setupBottomNavigation(userId)
    }

    private fun loadReportData() {
        val db = AppDatabase.getDatabase(this)

        lifecycleScope.launch {
            val userBudget = withContext(Dispatchers.IO) { db.budgetDao().getBudgetForUser(userId) }
            val budgetMax = userBudget?.maxSpend ?: 0.0

            val allExpenses = withContext(Dispatchers.IO) { db.expenseDao().getExpensesForUser(userId) }

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

            if (totalAmount > 0) {
                val chartDataList = mutableListOf<DonutChartView.ChartData>()
                val colors = listOf("#10A173", "#0D8A5D", "#F5A623", "#00A8E1", "#FF5252")
                var colorIndex = 0

                categoryMap.forEach { (_, amount) ->
                    val percentage = ((amount / totalAmount) * 100).toFloat()
                    val color = Color.parseColor(colors[colorIndex % colors.size])
                    chartDataList.add(DonutChartView.ChartData(percentage, color))
                    colorIndex++
                }

                val chart = findViewById<DonutChartView>(R.id.donutChartDynamic)
                chart?.setData(chartDataList)

                // Add Legend
                val legendContainer = findViewById<GridLayout>(R.id.legendContainer)
                legendContainer.removeAllViews()
                
                categoryMap.keys.forEachIndexed { index, category ->
                    val color = Color.parseColor(colors[index % colors.size])
                    val legendItem = createLegendItem(category, color)
                    legendContainer.addView(legendItem)
                }
            }
        }
    }

    private fun createLegendItem(name: String, color: Int): View {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(8, 8, 8, 8)
            val params = GridLayout.LayoutParams()
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            layoutParams = params
        }

        val colorCircle = View(this).apply {
            layoutParams = LinearLayout.LayoutParams(30, 30).apply {
                setMargins(0, 0, 16, 0)
            }
            setBackgroundResource(R.drawable.circle_bg)
            backgroundTintList = android.content.res.ColorStateList.valueOf(color)
        }

        val label = TextView(this).apply {
            text = name
            textSize = 14f
            setTextColor(Color.parseColor("#555555"))
        }

        layout.addView(colorCircle)
        layout.addView(label)
        return layout
    }

    // Helper property to use 'sp' in code
    private val Int.sp: Float get() = this * resources.displayMetrics.scaledDensity

    private fun setupBottomNavigation(userId: Int) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.selectedItemId = R.id.nav_reports
        
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
                R.id.nav_reports -> true
                R.id.nav_goals -> {
                    startActivity(Intent(this, BudgetActivity::class.java).apply { putExtra("USER_ID", userId) })
                    true
                }
                else -> false
            }
        }
    }
}
