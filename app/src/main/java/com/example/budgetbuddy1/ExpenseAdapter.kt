package com.example.budgetbuddy1

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetbuddy1.database.Expense

class ExpenseAdapter(private val expenses: List<Expense>) :
    RecyclerView.Adapter<ExpenseAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val info: TextView = view.findViewById(R.id.tvInfo)
        val image: ImageView = view.findViewById(R.id.imgExpense)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expense, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = expenses.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val exp = expenses[position]

        // TEXT
        holder.info.text =
            "R${exp.amount} | ${exp.category}\n${exp.date}"

        // IMAGE
        if (!exp.imageUri.isNullOrEmpty()) {
            holder.image.setImageURI(Uri.parse(exp.imageUri))
        } else {
            holder.image.setImageResource(android.R.color.transparent)
        }
    }
}