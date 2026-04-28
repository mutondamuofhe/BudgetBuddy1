package com.example.budgetbuddy1

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetbuddy1.database.AppDatabase
import java.text.SimpleDateFormat
import java.util.*

class ViewExpensesActivity : AppCompatActivity() {

    private var startDate = ""
    private var endDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_expense)

        val btnStart = findViewById<Button>(R.id.btnStartDate)
        val btnEnd = findViewById<Button>(R.id.btnEndDate)
        val btnFilter = findViewById<Button>(R.id.btnFilter)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewExpenses)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val tvTotal = findViewById<TextView>(R.id.tvTotal)
        val tvCategoryTotals = findViewById<TextView>(R.id.tvCategoryTotals)

        val userId = intent.getIntExtra("USER_ID", -1)
        val db = AppDatabase.getDatabase(this)
        val format = SimpleDateFormat("d/M/yyyy", Locale.getDefault())

        // DATE PICKERS
        btnStart.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(this,
                { _, y, m, d ->
                    startDate = "$d/${m + 1}/$y"
                    btnStart.text = startDate
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        btnEnd.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(this,
                { _, y, m, d ->
                    endDate = "$d/${m + 1}/$y"
                    btnEnd.text = endDate
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // FILTER BUTTON
        btnFilter.setOnClickListener {

            val expenses = db.expenseDao().getExpensesForUser(userId)

            val filtered = expenses.filter {
                if (startDate.isEmpty() || endDate.isEmpty()) return@filter true

                val expDate = try { format.parse(it.date) } catch (e: Exception) { null }
                val start = try { format.parse(startDate) } catch (e: Exception) { null }
                val end = try { format.parse(endDate) } catch (e: Exception) { null }

                expDate != null && start != null && end != null &&
                        !expDate.before(start) && !expDate.after(end)
            }

            val adapter = ExpenseAdapter(filtered)
            recyclerView.adapter = adapter

            // TOTAL
            val total = filtered.sumOf { it.amount }
            
            // LOAD BUDGET FROM DATABASE
            val userBudget = db.budgetDao().getBudgetForUser(userId)
            val min = userBudget?.minSpend ?: 0.0
            val max = userBudget?.maxSpend ?: Double.MAX_VALUE

            val status = when {
                total > max -> "❌ Over Budget!"
                total < min && min > 0 -> "⚠️ Below Minimum Target"
                else -> "✅ Within Budget"
            }

            tvTotal.text = "Total: R$total\n$status"

            // CATEGORY TOTALS
            val categoryMap = mutableMapOf<String, Double>()
            for (exp in filtered) {
                categoryMap[exp.category] =
                    categoryMap.getOrDefault(exp.category, 0.0) + exp.amount
            }

            val catText = StringBuilder("Category Totals:\n")
            for ((cat, amt) in categoryMap) {
                catText.append("$cat: R$amt\n")
            }
            tvCategoryTotals.text = catText.toString()
        }
    }
}