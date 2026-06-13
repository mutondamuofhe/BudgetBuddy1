package com.example.budgetbuddy1

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.budgetbuddy1.database.AppDatabase
import com.example.budgetbuddy1.database.Category
import com.example.budgetbuddy1.database.Expense
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import java.util.*

class AddExpenseActivity : AppCompatActivity() {

    private var selectedDate = ""
    private var startTime = ""
    private var endTime = ""
    private var imageUri: Uri? = null
    private val categoryList = mutableListOf<String>()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        val amount = findViewById<EditText>(R.id.etAmount)
        val description = findViewById<EditText>(R.id.etDescription)
        val spinner = findViewById<Spinner>(R.id.spCategory)
        val btnAddCategory = findViewById<ImageButton>(R.id.btnAddCategory)

        val btnDate = findViewById<Button>(R.id.btnDate)
        val btnStartTime = findViewById<Button>(R.id.btnStartTime)
        val btnEndTime = findViewById<Button>(R.id.btnEndTime)
        val btnImage = findViewById<Button>(R.id.btnSelectImage)
        val imageView = findViewById<ImageView>(R.id.imgPreview)
        val saveBtn = findViewById<Button>(R.id.btnSaveExpense)

        val userId = intent.getIntExtra("USER_ID", -1)
        val db = AppDatabase.getDatabase(this)

        // Setup Category Spinner
        adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Load Categories from Database
        loadCategories(db, userId)

        // Add New Category Dialog
        btnAddCategory.setOnClickListener {
            showAddCategoryDialog(db, userId)
        }

        // 📅 DATE PICKER
        btnDate.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(this, { _, y, m, d ->
                selectedDate = "$d/${m + 1}/$y"
                btnDate.text = selectedDate
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        // START TIME
        btnStartTime.setOnClickListener {
            val cal = Calendar.getInstance()
            TimePickerDialog(this, { _, hour, min ->
                startTime = "$hour:$min"
                btnStartTime.text = "Start: $startTime"
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        // END TIME
        btnEndTime.setOnClickListener {
            val cal = Calendar.getInstance()
            TimePickerDialog(this, { _, hour, min ->
                endTime = "$hour:$min"
                btnEndTime.text = "End: $endTime"
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        // IMAGE PICKER
        btnImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "image/*"
            }
            startActivityForResult(intent, 100)
        }

        // SAVE EXPENSE
        saveBtn.setOnClickListener {
            val amtText = amount.text.toString().trim()
            val desc = description.text.toString().trim()
            val cat = spinner.selectedItem?.toString() ?: ""

            if (amtText.isEmpty() || selectedDate.isEmpty()) {
                Toast.makeText(this, "Fill required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val amt = amtText.toDoubleOrNull()
            if (amt == null || amt <= 0) {
                Toast.makeText(this, "Enter valid amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val expense = Expense(
                userId = userId,
                amount = amt,
                category = cat,
                description = desc,
                date = selectedDate,
                startTime = startTime,
                endTime = endTime,
                imageUri = imageUri?.toString()
            )

            db.expenseDao().insertExpense(expense)
            Toast.makeText(this, "Expense Saved", Toast.LENGTH_SHORT).show()

            amount.text.clear()
            description.text.clear()
            btnDate.text = "Select Date"
            btnStartTime.text = "Start Time"
            btnEndTime.text = "End Time"
            imageView.visibility = ImageView.GONE
        }

        setupBottomNavigation(userId)
    }

    private fun loadCategories(db: AppDatabase, userId: Int) {
        lifecycleScope.launch {
            val categories = db.categoryDao().getCategoriesForUser(userId)
            if (categories.isEmpty()) {
                // Add defaults if none exist
                val defaults = listOf("Food", "Transport", "Shopping", "Bills")
                defaults.forEach { db.categoryDao().insertCategory(Category(name = it, userId = userId)) }
                categoryList.addAll(defaults)
            } else {
                categoryList.clear()
                categoryList.addAll(categories.map { it.name })
            }
            adapter.notifyDataSetChanged()
        }
    }

    private fun showAddCategoryDialog(db: AppDatabase, userId: Int) {
        val input = EditText(this)
        input.hint = "Category Name"
        input.setPadding(50, 40, 50, 40)

        AlertDialog.Builder(this)
            .setTitle("Add New Category")
            .setView(input)
            .setPositiveButton("Add") { _, _ ->
                val name = input.text.toString().trim()
                if (name.isNotEmpty()) {
                    lifecycleScope.launch {
                        val existing = db.categoryDao().getCategoryByName(name, userId)
                        if (existing == null) {
                            db.categoryDao().insertCategory(Category(name = name, userId = userId))
                            loadCategories(db, userId)
                        } else {
                            Toast.makeText(this@AddExpenseActivity, "Category exists", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            imageUri?.let {
                contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            val imageView = findViewById<ImageView>(R.id.imgPreview)
            imageView.visibility = ImageView.VISIBLE
            imageView.setImageURI(imageUri)
        }
    }

    private fun setupBottomNavigation(userId: Int) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.selectedItemId = R.id.nav_add
        
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
                R.id.nav_add -> true
                R.id.nav_reports -> {
                    startActivity(Intent(this, ReportsActivity::class.java).apply { putExtra("USER_ID", userId) })
                    true
                }
                R.id.nav_goals -> {
                    startActivity(Intent(this, BudgetActivity::class.java).apply { putExtra("USER_ID", userId) })
                    true
                }
                else -> false
            }
        }
    }
}
