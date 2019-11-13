package com.ober.drawerxarrowdrawable

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable

class DrawerXDrawable(context: Context) : DrawerArrowDrawable(context) {

    var mode = Mode.ARROW

    private val path = Path()

    override fun draw(canvas: Canvas) {
        if (mode == Mode.ARROW) {
            super.draw(canvas)
            return
        }

        val midWidth = lerp(barLength, 0F, progress)
        val topOffset = gapSize + paint.strokeWidth
        val startingY = lerp(topOffset, barLength / 2, progress)
        val dy = lerp(0F, startingY * 2, progress)

        path.rewind()

        path.moveTo(-midWidth / 2, 0F)
        path.rLineTo(midWidth, 0F)

        path.moveTo(-barLength / 2, startingY)
        path.rLineTo(barLength, -dy)

        path.moveTo(-barLength / 2, -startingY)
        path.rLineTo(barLength, dy)

        path.close()

        canvas.save()
        val barThickness = paint.strokeWidth
        val remainingSpace = (bounds.height().toFloat() - barThickness * 3 - gapSize * 2).toInt()
        var yOffset = (remainingSpace / 4 * 2).toFloat() // making sure it is a multiple of 2.
        yOffset += barThickness * 1.5f + gapSize
        canvas.translate(bounds.centerX().toFloat(), yOffset)
        canvas.drawPath(path, paint)
        canvas.restore()
    }

    /**
     * Linear interpolate between a and b with parameter t.
     */
    private fun lerp(a: Float, b: Float, t: Float): Float {
        return a + (b - a) * t
    }

    enum class Mode {
        ARROW,
        X
    }
}