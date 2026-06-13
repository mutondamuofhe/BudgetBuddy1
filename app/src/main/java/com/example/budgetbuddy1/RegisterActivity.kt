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

            if (user.isEmpty() || userEmail.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // PASSWORD VALIDATION: Must contain letters, numbers, and special characters
            if (!isValidPassword(pass)) {
                Toast.makeText(this, "Password must contain letters, numbers, and special characters", Toast.LENGTH_LONG).show()
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

    /**
     * Validates that the password contains at least one letter, one digit, and one special character.
     */
    private fun isValidPassword(password: String): Boolean {
        val hasLetter = password.any { it.isLetter() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecial = password.any { !it.isLetterOrDigit() }
        return hasLetter && hasDigit && hasSpecial && password.length >= 6
    }
}