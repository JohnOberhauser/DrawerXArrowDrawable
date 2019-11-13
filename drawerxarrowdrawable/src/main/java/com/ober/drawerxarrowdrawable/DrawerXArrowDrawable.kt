package com.ober.drawerxarrowdrawable

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.appcompat.R
import kotlin.math.roundToInt

class DrawerXArrowDrawable(private val context: Context, private var mode: Mode) : Drawable() {

    private val paint = Paint()

    private var currentTopBarAPosition = Position(0f, 0f)
    private var currentTopBarBPosition = Position(0f, 0f)

    private var currentMiddleBarAPosition = Position(0f, 0f)
    private var currentMiddleBarBPosition = Position(0f, 0f)

    private var currentBottomBarAPosition = Position(0f, 0f)
    private var currentBottomBarBPosition = Position(0f, 0f)

    private var targetTopBarAPosition = Position(0f, 0f)
    private var targetTopBarBPosition = Position(0f, 0f)

    private var targetMiddleBarAPosition = Position(0f, 0f)
    private var targetMiddleBarBPosition = Position(0f, 0f)

    private var targetBottomBarAPosition = Position(0f, 0f)
    private var targetBottomBarBPosition = Position(0f, 0f)

    private var transitioningTopBarAPosition = Position(0f, 0f)
    private var transitioningTopBarBPosition = Position(0f, 0f)

    private var transitioningMiddleBarAPosition = Position(0f, 0f)
    private var transitioningMiddleBarBPosition = Position(0f, 0f)

    private var transitioningBottomBarAPosition = Position(0f, 0f)
    private var transitioningBottomBarBPosition = Position(0f, 0f)

    private var currentRotation = 0f
    private var targetRotation = 0f
    private var transitioningRotation = 0f
        set(value) {
            field = if (value >= 360) {
                value - 360
            } else {
                value
            }
        }

    private var progress = 0f
        set(value) {
            if (field != value) {
                field = value
                invalidateSelf()
            }
        }

    private var isFlipped = false
    private var size = 0

    var gap = 0f
        set(value) {
            if (field != value) {
                field = value
                invalidateSelf()
            }
        }

    var spin = true
        set(value) {
            if (field != value) {
                field = value
                invalidateSelf()
            }
        }

    var barLength = 0f
    var arrowShaftLength = 0f
    var arrowHeadLength = 0f

    var defaultDuration: Long = 250

    var color: Int
        set(@ColorInt value) {
            if (value != paint.color) {
                paint.color = value
                invalidateSelf()
            }
        }
        get() = paint.color

    var barThickness: Float
        set(value) {
            if (paint.strokeWidth != value) {
                paint.strokeWidth = value
                invalidateSelf()
            }
        }
        get() = paint.strokeWidth

    init {
        init()
    }

    @SuppressLint("PrivateResource")
    private fun init() {
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.MITER
        paint.strokeCap = Paint.Cap.BUTT
        paint.isAntiAlias = true

        val a = context.theme.obtainStyledAttributes(
            null,
            R.styleable.DrawerArrowToggle, R.attr.drawerArrowStyle,
            R.style.Base_Widget_AppCompat_DrawerArrowToggle
        )

        color = a.getColor(R.styleable.DrawerArrowToggle_color, 0)
        barThickness = a.getDimension(R.styleable.DrawerArrowToggle_thickness, 0f)
        spin = a.getBoolean(R.styleable.DrawerArrowToggle_spinBars, true)
        // round this because having this floating may cause bad measurements
        gap = a.getDimension(R.styleable.DrawerArrowToggle_gapBetweenBars, 0f).roundToInt().toFloat()

        size = a.getDimensionPixelSize(R.styleable.DrawerArrowToggle_drawableSize, 0)
        // round this because having this floating may cause bad measurements
        barLength = a.getDimension(R.styleable.DrawerArrowToggle_barLength, 0f).roundToInt().toFloat()
        arrowHeadLength = a.getDimension(
            R.styleable.DrawerArrowToggle_arrowHeadLength, 0f
        ).roundToInt().toFloat()
        arrowShaftLength = a.getDimension(R.styleable.DrawerArrowToggle_arrowShaftLength, 0f)
        a.recycle()
    }

    override fun onBoundsChange(bounds: Rect?) {
        setMode(mode, duration = 0, override = true)
    }

    override fun setAlpha(alpha: Int) {
        if (alpha != paint.alpha) {
            paint.alpha = alpha
            invalidateSelf()
        }
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
        invalidateSelf()
    }

    override fun getIntrinsicWidth(): Int {
        return size
    }

    override fun getIntrinsicHeight(): Int {
        return size
    }

    override fun draw(canvas: Canvas) {
        val path = Path()

        val newTopPositions = drawLine(
            currentTopBarAPosition,
            currentTopBarBPosition,
            targetTopBarAPosition,
            targetTopBarBPosition,
            path
        )
        transitioningTopBarAPosition = newTopPositions[0]
        transitioningTopBarBPosition = newTopPositions[1]

        val newMiddlePositions = drawLine(
            currentMiddleBarAPosition,
            currentMiddleBarBPosition,
            targetMiddleBarAPosition,
            targetMiddleBarBPosition,
            path
        )
        transitioningMiddleBarAPosition = newMiddlePositions[0]
        transitioningMiddleBarBPosition = newMiddlePositions[1]

        val newBottomPositions = drawLine(
            currentBottomBarAPosition,
            currentBottomBarBPosition,
            targetBottomBarAPosition,
            targetBottomBarBPosition,
            path
        )
        transitioningBottomBarAPosition = newBottomPositions[0]
        transitioningBottomBarBPosition = newBottomPositions[1]

        path.close()
        canvas.save()
        transitioningRotation = lerp(currentRotation, targetRotation, progress)
        if (spin) {
            canvas.rotate(transitioningRotation, centerX(), centerY())
        }
        canvas.drawPath(path, paint)
        canvas.restore()
    }

    private fun drawLine(
        currentPositionA: Position,
        currentPositionB: Position,
        targetPositionA: Position,
        targetPositionB: Position,
        path: Path
    ): Array<Position> {
        val positionA = Position(
            lerp(currentPositionA.x, targetPositionA.x, progress),
            lerp(currentPositionA.y, targetPositionA.y, progress)
        )

        val positionB = Position(
            lerp(currentPositionB.x, targetPositionB.x, progress),
            lerp(currentPositionB.y, targetPositionB.y, progress)
        )

        path.moveTo(positionA.x, positionA.y)
        path.rLineTo(positionB.x - positionA.x, positionB.y - positionA.y)
        return arrayOf(positionA, positionB)
    }


    /**
     * Linear interpolate between a and b with parameter t.
     */
    private fun lerp(a: Float, b: Float, t: Float): Float {
        return a + ((b - a) * t)
    }

    fun setMode(mode: Mode, duration: Long = defaultDuration) {
        setMode(mode, duration, false)
    }

    private fun setMode(mode: Mode, duration: Long, override: Boolean) {
        if (this.mode == mode && !override) {
            return
        }
        this.mode = mode
        progress = 0f
        currentTopBarAPosition = transitioningTopBarAPosition
        currentTopBarBPosition = transitioningTopBarBPosition
        currentMiddleBarAPosition = transitioningMiddleBarAPosition
        currentMiddleBarBPosition = transitioningMiddleBarBPosition
        currentBottomBarAPosition = transitioningBottomBarAPosition
        currentBottomBarBPosition = transitioningBottomBarBPosition

        currentRotation = transitioningRotation

        if (spin) {
            when (currentRotation) {
                in 0.0..90.0 -> {
                    targetRotation = 180f
                    isFlipped = false
                }
                in 90.0..270.0 -> {
                    targetRotation = 360f
                    isFlipped = true
                }
                else -> {
                    targetRotation = 540f
                    isFlipped = false
                }
            }
        }

        when (mode) {
            Mode.DRAWER -> {
                targetDrawer()
            }
            Mode.ARROW -> {
                if (spin) {
                    if (isFlipped) {
                        targetLeftArrow()
                    } else {
                        targetRightArrow()
                    }
                } else {
                    targetLeftArrow()
                }
            }
            Mode.X -> {
                targetX()
            }
        }

        ValueAnimator.ofFloat(0f, 1f).apply {
            setDuration(duration)
            addUpdateListener {
                progress = it.animatedValue as Float
            }
            start()
        }
    }

    private fun centerX(): Float {
        return bounds.exactCenterX()
    }

    private fun centerY(): Float {
        return bounds.exactCenterY()
    }

    private fun targetDrawer() {
        targetTopBarAPosition = Position(drawerLeft(), drawerTop())
        targetTopBarBPosition = Position(drawerRight(), drawerTop())
        targetMiddleBarAPosition = Position(drawerLeft(), drawerCenterY())
        targetMiddleBarBPosition = Position(drawerRight(), drawerCenterY())
        targetBottomBarAPosition = Position(drawerLeft(), drawerBottom())
        targetBottomBarBPosition = Position(drawerRight(), drawerBottom())
    }

    private fun drawerLeft(): Float {
        return centerX() - (barLength / 2)
    }

    private fun drawerRight(): Float {
        return centerX() + (barLength / 2)
    }

    private fun drawerTop(): Float {
        return centerY() - ((paint.strokeWidth) + gap)
    }

    private fun drawerBottom(): Float {
        return centerY() + ((paint.strokeWidth) + gap)
    }

    private fun drawerCenterY(): Float {
        return centerY()
    }

    private fun targetLeftArrow() {
        targetTopBarAPosition = Position(arrowLeft(), arrowCenterY())
        targetTopBarBPosition = Position(arrowLeftOffset(), arrowTop())
        targetMiddleBarAPosition = Position(arrowLeft(), arrowCenterY())
        targetMiddleBarBPosition = Position(arrowRight(), arrowCenterY())
        targetBottomBarAPosition = Position(arrowLeft(), arrowCenterY())
        targetBottomBarBPosition = Position(arrowLeftOffset(), arrowBottom())
    }

    private fun targetRightArrow() {
        targetTopBarAPosition = Position(arrowRightOffset(), arrowTop())
        targetTopBarBPosition = Position(arrowRight(), arrowCenterY())
        targetMiddleBarAPosition = Position(arrowLeft(), arrowCenterY())
        targetMiddleBarBPosition = Position(arrowRight(), arrowCenterY())
        targetBottomBarAPosition = Position(arrowRightOffset(), arrowBottom())
        targetBottomBarBPosition = Position(arrowRight(), arrowCenterY())
    }

    private fun arrowLeft(): Float {
        return centerX() - (arrowShaftLength / 2)
    }

    private fun arrowRight(): Float {
        return centerX() + (arrowShaftLength / 2)
    }

    private fun arrowCenterY(): Float {
        return centerY()
    }

    private fun arrowTop(): Float {
        return centerY() - arrowHeadLength
    }

    private fun arrowBottom(): Float {
        return centerY() + arrowHeadLength
    }

    private fun arrowLeftOffset(): Float {
        return arrowLeft() + arrowHeadLength
    }

    private fun arrowRightOffset(): Float {
        return arrowRight() - arrowHeadLength
    }

    private fun targetX() {
        targetTopBarAPosition = Position(xLeft(), xTop())
        targetTopBarBPosition = Position(xRight(), xBottom())
        targetMiddleBarAPosition = Position(xCenterX(), xCenterY())
        targetMiddleBarBPosition = Position(xCenterX(), xCenterY())
        targetBottomBarAPosition = Position(xLeft(), xBottom())
        targetBottomBarBPosition = Position(xRight(), xTop())
    }

    private fun xLeft(): Float {
        return centerX() - ((barLength * 0.85f) / 2)
    }

    private fun xRight(): Float {
        return centerX() + ((barLength * 0.85f) / 2)
    }

    private fun xTop(): Float {
        return centerY() - ((barLength * 0.85f) / 2)
    }

    private fun xBottom(): Float {
        return centerY() + ((barLength * 0.85f) / 2)
    }

    private fun xCenterX(): Float {
        return centerX()
    }

    private fun xCenterY(): Float {
        return centerY()
    }

    enum class Mode {
        DRAWER,
        ARROW,
        X
    }
}