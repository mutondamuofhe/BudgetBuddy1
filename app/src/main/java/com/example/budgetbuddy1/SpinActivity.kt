package com.example.budgetbuddy1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.budgetbuddy1.database.AppDatabase
import com.example.budgetbuddy1.database.Reward
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class SpinActivity : AppCompatActivity() {

    private var isSpinning = false
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spin)

        userId = intent.getIntExtra("USER_ID", -1)
        val db = AppDatabase.getDatabase(this)

        val luckyWheel = findViewById<LuckyWheelView>(R.id.luckyWheel)
        val btnSpin = findViewById<Button>(R.id.btnSpin)
        val tvResult = findViewById<TextView>(R.id.tvResult)
        val tvBalance = findViewById<TextView>(R.id.tvRewardBalance)
        val tvSpinMessage = findViewById<TextView>(R.id.tvSpinMessage)

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = sdf.format(Date())

        lifecycleScope.launch {
            // Check if already spun today
            val latestReward = db.rewardDao().getLatestRewardForUser(userId)
            val allRewards = db.rewardDao().getAllRewardsForUser(userId)
            val totalBalance = allRewards.sumOf { it.amount }

            // Update UI with current total balance
            tvBalance.text = "Balance: R${String.format("%.2f", totalBalance)}"

            if (latestReward?.spinDate == today) {
                btnSpin.isEnabled = false
                btnSpin.alpha = 0.5f
                btnSpin.text = "Spun Today"
                tvSpinMessage.text = "Come back tomorrow for more rewards!"
                tvResult.text = "You've already claimed your reward for today."
            }

            btnSpin.setOnClickListener {
                if (isSpinning) return@setOnClickListener
                isSpinning = true
                val randomDegrees = (Random.nextInt(5, 10) * 360) + Random.nextInt(0, 360)

                luckyWheel.startSpinning(randomDegrees.toFloat()) { index ->
                    isSpinning = false
                    val rewardText = luckyWheel.getSegmentText(index).replace("\n", " ")
                    val rewardAmount = if (rewardText.contains("R")) rewardText.filter { it.isDigit() }.toDoubleOrNull() ?: 0.0 else 0.0

                    lifecycleScope.launch {
                        val newReward = Reward(userId = userId, amount = rewardAmount, spinDate = today)
                        db.rewardDao().saveReward(newReward)

                        val updatedAllRewards = db.rewardDao().getAllRewardsForUser(userId)
                        val newTotalBalance = updatedAllRewards.sumOf { it.amount }

                        tvResult.text = "Congratulations!\nYou won $rewardText"
                        tvBalance.text = "Balance: R${String.format("%.2f", newTotalBalance)}"
                        btnSpin.isEnabled = false
                        btnSpin.alpha = 0.5f
                        btnSpin.text = "Spun Today"
                        tvSpinMessage.text = "Come back tomorrow!"
                        Toast.makeText(this@SpinActivity, "You won $rewardText!", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        setupBottomNavigation(userId)
    }

    private fun setupBottomNavigation(userId: Int) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        // No specific ID for Rewards in the menu yet, but we'll leave it unselected or highlight Home
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
