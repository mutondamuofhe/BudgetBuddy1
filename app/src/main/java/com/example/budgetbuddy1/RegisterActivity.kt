package com.example.budgetbuddy1

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.budgetbuddy1.database.AppDatabase
import com.example.budgetbuddy1.database.User
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val username = findViewById<EditText>(R.id.etNewUsername)
        val email = findViewById<EditText>(R.id.etEmail)
        val password = findViewById<EditText>(R.id.etNewPassword)
        val cbAgreeTerms = findViewById<CheckBox>(R.id.cbAgreeTerms)
        val btnRegister = findViewById<Button>(R.id.btnSave)
        val tvLoginLink = findViewById<TextView>(R.id.tvLoginLink)

        val db = AppDatabase.getDatabase(this)

        tvLoginLink.setOnClickListener {
            finish() // Go back to Login
        }

        btnRegister.setOnClickListener {

            val user = username.text.toString().trim()
            val userEmail = email.text.toString().trim()
            val pass = password.text.toString().trim()
            val isTermsAccepted = cbAgreeTerms.isChecked

            if (user.isEmpty() || userEmail.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isTermsAccepted) {
                Toast.makeText(this, "Please accept Terms of Service", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Use coroutine for database operations
            lifecycleScope.launch {
                // Check if username already exists
                val existingUser = db.userDao().getUser(user)

                if (existingUser != null) {
                    Toast.makeText(this@RegisterActivity, "Username already exists", Toast.LENGTH_SHORT).show()
                } else {
                    // Insert new user with email
                    db.userDao().insertUser(User(
                        username = user,
                        email = userEmail,
                        password = pass
                    ))
                    Toast.makeText(this@RegisterActivity, "Registration Successful", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}