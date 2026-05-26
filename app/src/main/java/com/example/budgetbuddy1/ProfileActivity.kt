package com.example.budgetbuddy1

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.budgetbuddy1.database.AppDatabase
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val emailInput = findViewById<EditText>(R.id.etProfileEmail)
        val phoneInput = findViewById<EditText>(R.id.etProfilePhone)
        val passwordInput = findViewById<EditText>(R.id.etProfilePassword)
        val updateBtn = findViewById<Button>(R.id.btnUpdateProfile)
        val backBtn = findViewById<Button>(R.id.btnProfileBack)

        val userId = intent.getIntExtra("USER_ID", -1)
        val db = AppDatabase.getDatabase(this)

        // Load current user data
        lifecycleScope.launch {
            val user = db.userDao().getUserById(userId)
            user?.let {
                emailInput.setText(it.email)
                phoneInput.setText(it.phoneNumber)
            }
        }

        backBtn.setOnClickListener { finish() }

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
                    // If password field is not empty, validate and update it
                    val updatedPassword = if (newPass.isNotEmpty()) {
                        if (!isValidPassword(newPass)) {
                            Toast.makeText(this@ProfileActivity, "Password must contain letters, numbers, and special characters", Toast.LENGTH_LONG).show()
                            return@launch
                        }
                        newPass
                    } else {
                        user.password
                    }

                    val updatedUser = user.copy(
                        email = newEmail,
                        phoneNumber = newPhone,
                        password = updatedPassword
                    )

                    db.userDao().updateUser(updatedUser)
                    Toast.makeText(this@ProfileActivity, "Profile Updated", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun isValidPassword(password: String): Boolean {
        val hasLetter = password.any { it.isLetter() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecial = password.any { !it.isLetterOrDigit() }
        return hasLetter && hasDigit && hasSpecial && password.length >= 6
    }
}
