package com.example.budgetbuddy1

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class DonutChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 60f
        strokeCap = Paint.Cap.BUTT
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 30f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
    }

    private var data: List<ChartData> = emptyList()
    private val rectF = RectF()

    fun setData(newData: List<ChartData>) {
        this.data = newData
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (data.isEmpty()) return

        val size = Math.min(width, height).toFloat()
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = size / 2f - paint.strokeWidth

        rectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius)

        var startAngle = -90f
        for (item in data) {
            if (item.percentage <= 0) continue
            
            paint.color = item.color
            val sweepAngle = (item.percentage / 100f) * 360f
            canvas.drawArc(rectF, startAngle, sweepAngle, false, paint)

            // Draw percentage text if enough space
            if (item.percentage > 5) {
                val middleAngle = Math.toRadians((startAngle + sweepAngle / 2).toDouble())
                val textX = centerX + radius * Math.cos(middleAngle).toFloat()
                val textY = centerY + radius * Math.sin(middleAngle).toFloat() + 10f
                canvas.drawText("${item.percentage.toInt()}%", textX, textY, textPaint)
            }

            startAngle += sweepAngle
        }
    }

    data class ChartData(val percentage: Float, val color: Int)
}
