package com.example.budgetbuddy1

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.budgetbuddy1.database.AppDatabase
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initializing UI components from layout
        val username = findViewById<EditText>(R.id.etUsername)
        val password = findViewById<EditText>(R.id.etPassword)
        val loginBtn = findViewById<Button>(R.id.btnLogin)
        val registerBtn = findViewById<TextView>(R.id.btnRegister)

        // Initialize Room Database instance
        val db = AppDatabase.getDatabase(this)

        // Login button logic
        loginBtn.setOnClickListener {
            val user = username.text.toString().trim()
            val pass = password.text.toString().trim()

            // Run database query on a background thread using lifecycleScope
            lifecycleScope.launch {
                val loggedUser = db.userDao().login(user, pass)

                if (loggedUser != null) {
                    // Success: Navigate to Dashboard and pass user info via Intent extras
                    Toast.makeText(this@MainActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@MainActivity, DashboardActivity::class.java)
                    intent.putExtra("USERNAME", loggedUser.username)
                    intent.putExtra("USER_ID", loggedUser.id)
                    startActivity(intent)
                    finish() // Close login screen
                } else {
                    // Failure: Show error message
                    Toast.makeText(this@MainActivity, "Invalid Credentials", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Navigate to Registration screen
        registerBtn.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}