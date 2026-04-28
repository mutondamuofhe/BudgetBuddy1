package com.example.budgetbuddy1

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class BarChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val barPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#00A8E1") // Blue
        style = Paint.Style.FILL
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#888888")
        textSize = 24f
        textAlign = Paint.Align.CENTER
    }

    private val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#EEEEEE")
        strokeWidth = 2f
    }

    private val data = listOf(
        BarData("Jan", 3000f),
        BarData("Feb", 4500f),
        BarData("Mar", 3200f),
        BarData("Apr", 4800f)
    )

    private val maxValue = 6000f

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val paddingLeft = 80f
        val paddingBottom = 60f
        val chartWidth = width - paddingLeft - 40f
        val chartHeight = height - paddingBottom - 40f
        val barWidth = chartWidth / (data.size * 2)

        // Draw Y-axis labels
        for (i in 0..3) {
            val y = chartHeight - (i * chartHeight / 3) + 20f
            val label = "R${(i * 2)}k"
            canvas.drawText(label, 40f, y, textPaint)
            canvas.drawLine(paddingLeft, y - 10f, width.toFloat(), y - 10f, gridPaint)
        }

        // Draw Bars
        for (i in data.indices) {
            val item = data[i]
            val barHeight = (item.value / maxValue) * chartHeight
            val left = paddingLeft + (i * 2 + 0.5f) * barWidth
            val top = chartHeight - barHeight + 20f
            val right = left + barWidth
            val bottom = chartHeight + 20f

            val rectF = RectF(left, top, right, bottom)
            canvas.drawRoundRect(rectF, 10f, 10f, barPaint)

            // Draw Month Label
            canvas.drawText(item.month, (left + right) / 2f, height.toFloat() - 10f, textPaint)
        }
    }

    data class BarData(val month: String, val value: Float)
}
