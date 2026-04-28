package com.example.budgetbuddy1

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import kotlin.math.cos
import kotlin.math.sin

class LuckyWheelView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val segments = listOf(
        WheelSegment("R5\nCashback", Color.parseColor("#10A173")), // Green
        WheelSegment("R10\nCashback", Color.parseColor("#F5A623")), // Orange/Yellow
        WheelSegment("R5\nCashback", Color.parseColor("#00A8E1")), // Blue
        WheelSegment("50\nPoints", Color.parseColor("#F8E71C")), // Yellow
        WheelSegment("R5\nCashback", Color.parseColor("#10A173")), // Green
        WheelSegment("R5\nCashback", Color.parseColor("#F5A623")), // Orange
        WheelSegment("50\nPoints", Color.parseColor("#00A8E1")), // Blue
        WheelSegment("R5\nCashback", Color.parseColor("#10A173"))  // Green
    )

    private val segmentPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 40f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
    }
    private val centerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }
    private val centerStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#F5A623")
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }
    private val spinTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textSize = 50f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
    }
    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#F5A623")
        style = Paint.Style.STROKE
        strokeWidth = 20f
    }

    private var rotationAngle = 0f

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val size = Math.min(width, height).toFloat()
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = size / 2f - 40f

        // Draw the rotating part
        canvas.save()
        canvas.rotate(rotationAngle, centerX, centerY)

        val rectF = RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius)
        val anglePerSegment = 360f / segments.size

        for (i in segments.indices) {
            segmentPaint.color = segments[i].color
            canvas.drawArc(rectF, i * anglePerSegment, anglePerSegment, true, segmentPaint)

            // Draw text
            canvas.save()
            val textAngle = i * anglePerSegment + anglePerSegment / 2f
            canvas.rotate(textAngle, centerX, centerY)
            
            val x = centerX + (radius * 0.65f)
            val y = centerY
            
            canvas.rotate(90f, x, y)
            
            val lines = segments[i].text.split("\n")
            var currentY = y - (lines.size - 1) * 20f
            for (line in lines) {
                canvas.drawText(line, x, currentY, textPaint)
                currentY += 45f
            }

            // Draw a small "badge" icon for points segments
            if (segments[i].text.contains("Points")) {
                val badgePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = Color.parseColor("#FFD700") // Gold
                    style = Paint.Style.FILL
                }
                canvas.drawRect(x - 15f, currentY + 10f, x + 15f, currentY + 30f, badgePaint)
            }
            
            canvas.restore()
        }
        canvas.restore()

        // Draw stationary parts
        // Draw Border with dots like the image
        canvas.drawCircle(centerX, centerY, radius, borderPaint)
        
        val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            style = Paint.Style.FILL
        }
        for (i in 0 until 12) {
            val angle = i * 30f
            val dx = centerX + (radius) * Math.cos(Math.toRadians(angle.toDouble())).toFloat()
            val dy = centerY + (radius) * Math.sin(Math.toRadians(angle.toDouble())).toFloat()
            canvas.drawCircle(dx, dy, 5f, dotPaint)
        }

        // Draw Center Button
        val centerRadius = radius * 0.25f
        canvas.drawCircle(centerX, centerY, centerRadius, centerPaint)
        canvas.drawCircle(centerX, centerY, centerRadius, centerStrokePaint)
        canvas.drawText("SPIN", centerX, centerY + 15f, spinTextPaint)

        // Draw Indicator (at the top, stationary)
        val indicatorPath = Path().apply {
            moveTo(centerX, centerY - radius - 20f)
            lineTo(centerX - 25f, centerY - radius + 30f)
            lineTo(centerX + 25f, centerY - radius + 30f)
            close()
        }
        val indicatorPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#333333")
            style = Paint.Style.FILL
        }
        canvas.drawPath(indicatorPath, indicatorPaint)
        
        // Golden knob for indicator
        canvas.drawCircle(centerX, centerY - radius - 25f, 15f, centerStrokePaint)
    }

    fun startSpinning(targetAngle: Float, onFinished: (Int) -> Unit) {
        val animator = android.animation.ValueAnimator.ofFloat(rotationAngle, rotationAngle + targetAngle)
        animator.duration = 3500
        animator.interpolator = DecelerateInterpolator()
        animator.addUpdateListener { animation ->
            rotationAngle = animation.animatedValue as Float
            invalidate()
        }
        animator.addListener(object : android.animation.AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: android.animation.Animator) {
                val anglePerSegment = 360f / segments.size
                // Calculate which segment is at the top (270 degrees)
                // The drawing starts at 0 degrees (right). The indicator is at 270 degrees (top).
                val normalizedRotation = (270f - (rotationAngle % 360f) + 360f) % 360f
                val index = (normalizedRotation / anglePerSegment).toInt() % segments.size
                onFinished(index)
            }
        })
        animator.start()
    }

    fun getSegmentText(index: Int): String = segments[index].text

    data class WheelSegment(val text: String, val color: Int)
}
