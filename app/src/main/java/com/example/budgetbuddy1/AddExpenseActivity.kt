package com.example.budgetbuddy1

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.budgetbuddy1.database.AppDatabase
import com.example.budgetbuddy1.database.Expense
import java.util.*

class AddExpenseActivity : AppCompatActivity() {

    private var selectedDate = ""
    private var startTime = ""
    private var endTime = ""
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        val amount = findViewById<EditText>(R.id.etAmount)
        val description = findViewById<EditText>(R.id.etDescription)
        val spinner = findViewById<Spinner>(R.id.spCategory)

        val btnDate = findViewById<Button>(R.id.btnDate)
        val btnStartTime = findViewById<Button>(R.id.btnStartTime)
        val btnEndTime = findViewById<Button>(R.id.btnEndTime)
        val btnImage = findViewById<Button>(R.id.btnSelectImage)
        val imageView = findViewById<ImageView>(R.id.imgPreview)
        val saveBtn = findViewById<Button>(R.id.btnSaveExpense)

        val userId = intent.getIntExtra("USER_ID", -1)
        val db = AppDatabase.getDatabase(this)

        // 🔹 Categories (you can improve later)
        val categories = listOf("Food", "Transport", "Shopping", "Bills")
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            categories
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // 📅 DATE PICKER
        btnDate.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(this,
                { _, y, m, d ->
                    selectedDate = "$d/${m + 1}/$y"
                    btnDate.text = selectedDate
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // START TIME
        btnStartTime.setOnClickListener {
            val cal = Calendar.getInstance()
            TimePickerDialog(this,
                { _, hour, min ->
                    startTime = "$hour:$min"
                    btnStartTime.text = "Start: $startTime"
                },
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }

        //  END TIME
        btnEndTime.setOnClickListener {
            val cal = Calendar.getInstance()
            TimePickerDialog(this,
                { _, hour, min ->
                    endTime = "$hour:$min"
                    btnEndTime.text = "End: $endTime"
                },
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }

        //  IMAGE PICKER: Uses ACTION_OPEN_DOCUMENT to allow selecting files from storage
        btnImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, 100)
        }

        //  SAVE EXPENSE: Validates input and saves to RoomDB
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

            // Create Expense object
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

            // Insert into Database
            db.expenseDao().insertExpense(expense)

            Toast.makeText(this, "Expense Saved", Toast.LENGTH_SHORT).show()

            // Clear fields
            amount.text.clear()
            description.text.clear()
            btnDate.text = "Select Date"
            btnStartTime.text = "Start Time"
            btnEndTime.text = "End Time"
            imageView.setImageDrawable(null)
        }
    }

    //  HANDLE IMAGE RESULT: Takes persistable permission to ensure URI access after app restart
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            imageUri?.let {
                // Securing persistable permission for the selected image
                contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
            val imageView = findViewById<ImageView>(R.id.imgPreview)
            imageView.setImageURI(imageUri)
        }
    }
}