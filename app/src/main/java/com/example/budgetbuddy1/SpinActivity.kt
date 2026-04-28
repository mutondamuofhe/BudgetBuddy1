package com.example.budgetbuddy1

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.budgetbuddy1.database.AppDatabase
import com.example.budgetbuddy1.database.Reward
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
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val tvResult = findViewById<TextView>(R.id.tvResult)
        val tvBalance = findViewById<TextView>(R.id.tvRewardBalance)
        val tvSpinMessage = findViewById<TextView>(R.id.tvSpinMessage)

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = sdf.format(Date())

        // Load Reward Data
        var userReward = db.rewardDao().getRewardForUser(userId)
        if (userReward == null) {
            userReward = Reward(userId, 0.0, "")
        }

        // Update UI with current balance
        tvBalance.text = "Balance: R${String.format("%.2f", userReward.totalBalance)}"

        // Check if already spun today
        if (userReward.lastSpinDate == today) {
            btnSpin.isEnabled = false
            btnSpin.alpha = 0.5f
            btnSpin.text = "Spun Today"
            tvSpinMessage.text = "Come back tomorrow for more rewards!"
            tvResult.text = "You've already claimed your reward for today.\nSee you tomorrow!"
        }

        btnBack.setOnClickListener {
            finish()
        }

        btnSpin.setOnClickListener {
            if (isSpinning) return@setOnClickListener

            isSpinning = true
            
            // Generate a random degree (at least 5 full rotations + some extra)
            val randomDegrees = (Random.nextInt(5, 10) * 360) + Random.nextInt(0, 360)
            
            luckyWheel.startSpinning(randomDegrees.toFloat()) { index ->
                isSpinning = false
                val rewardText = luckyWheel.getSegmentText(index).replace("\n", " ")
                
                // Extract amount from reward text (e.g., "R5", "R10", "Badge")
                val rewardAmount = if (rewardText.contains("R")) {
                    rewardText.filter { it.isDigit() }.toDoubleOrNull() ?: 0.0
                } else {
                    0.0 // Non-cash reward
                }

                // Update Database (Moved into a thread-safe way)
                val currentBalance = userReward?.totalBalance ?: 0.0
                val newBalance = currentBalance + rewardAmount
                val updatedReward = Reward(userId, newBalance, today)
                
                // Important: Update the local variable so subsequent logic uses the correct value
                userReward = updatedReward

                db.rewardDao().saveReward(updatedReward)

                // Update UI
                tvResult.text = "Congratulations!\nYou won $rewardText"
                tvBalance.text = "Balance: R${String.format("%.2f", newBalance)}"
                
                btnSpin.isEnabled = false
                btnSpin.alpha = 0.5f
                btnSpin.text = "Spun Today"
                tvSpinMessage.text = "Come back tomorrow for more rewards!"
                
                Toast.makeText(this, "You won $rewardText!", Toast.LENGTH_LONG).show()
            }
        }
    }
}
