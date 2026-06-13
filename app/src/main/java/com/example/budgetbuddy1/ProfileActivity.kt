package com.example.budgetbuddy1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.example.budgetbuddy1.database.AppDatabase
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.switchmaterial.SwitchMaterial
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val emailInput = findViewById<EditText>(R.id.etProfileEmail)
        val phoneInput = findViewById<EditText>(R.id.etProfilePhone)
        val passwordInput = findViewById<EditText>(R.id.etProfilePassword)
        val updateBtn = findViewById<Button>(R.id.btnUpdateProfile)
        val themeSwitch = findViewById<SwitchMaterial>(R.id.switchDarkMode)

        // Theme preference
        val sharedPref = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val isDarkMode = sharedPref.getBoolean("DarkMode", false)
        themeSwitch.isChecked = isDarkMode

        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPref.edit().putBoolean("DarkMode", isChecked).apply()
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        val userId = intent.getIntExtra("USER_ID", -1)
        val db = AppDatabase.getDatabase(this)

        lifecycleScope.launch {
            val user = db.userDao().getUserById(userId)
            user?.let {
                emailInput.setText(it.email)
                phoneInput.setText(it.phoneNumber)
            }
        }

        updateBtn.setOnClickListener {
            val newEmail = emailInput.text.toString().trim()
            val newPhone = phoneInput.text.toString().trim()
            val newPass = passwordInput.text.toString().trim()

            if (newEmail.isEmpty()) {
                Toast.makeText(this, "Email cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val user = db.userDao().getUserById(userId)
                if (user != null) {
                    val updatedPassword = if (newPass.isNotEmpty()) {
                        if (!isValidPassword(newPass)) {
                            Toast.makeText(this@ProfileActivity, "Password too weak", Toast.LENGTH_LONG).show()
                            return@launch
                        }
                        newPass
                    } else user.password

                    val updatedUser = user.copy(email = newEmail, phoneNumber = newPhone, password = updatedPassword)
                    db.userDao().updateUser(updatedUser)
                    Toast.makeText(this@ProfileActivity, "Profile Updated", Toast.LENGTH_SHORT).show()
                }
            }
        }

        setupBottomNavigation(userId)
    }

    private fun isValidPassword(password: String): Boolean {
        return password.any { it.isLetter() } && password.any { it.isDigit() } && password.any { !it.isLetterOrDigit() } && password.length >= 6
    }

    private fun setupBottomNavigation(userId: Int) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.selectedItemId = 0 
        
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
                R.id.nav_goals -> {
                    startActivity(Intent(this, BudgetActivity::class.java).apply { putExtra("USER_ID", userId) })
                    true
                }
                else -> false
            }
        }
    }
}
