package me.aflak.androidistalking

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.core.animation.doOnEnd
import me.aflak.libraries.SinStateProvider
import me.aflak.libraries.StateProvider

class AndroidIsTalking @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val barPaint: Paint
    private val barPositions: List<RectF>
    private val canvasWidth: Float
    private var states: List<List<Float>>
    private var currentState: ArrayList<Float>
    private var animatorSet: AnimatorSet

    private val barCount: Int
    private val barColor: Int
    private val barWidth: Float
    private val barSpace: Float
    private val animationDuration: Long

    var stateProvider: StateProvider = SinStateProvider
        set(value) {
            field = value
            states = value.getStates(barCount)
            currentState = ArrayList(states.first())
            animatorSet.doOnEnd {}
            animatorSet.cancel()
            animatorSet = buildAnimation()
            start()
        }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.AndroidIsTalking,
            0,
            0
        ).apply {
            try {
                barCount = getInteger(R.styleable.AndroidIsTalking_barCount, DEFAULT_BAR_COUNT)
                barColor = getInteger(R.styleable.AndroidIsTalking_barColor, DEFAULT_BAR_COLOR)
                barWidth = getDimension(R.styleable.AndroidIsTalking_barWidth, DEFAULT_BAR_WIDTH)
                barSpace = getDimension(R.styleable.AndroidIsTalking_barSpace, DEFAULT_BAR_SPACE)
                animationDuration = getInteger(
                    R.styleable.AndroidIsTalking_animationDuration,
                    DEFAULT_ANIMATION_DURATION_MS
                ).toLong()
            } finally {
                recycle()
            }
        }

        canvasWidth = barWidth * barCount + barSpace * (barCount - 1) + paddingLeft + paddingRight
        barPositions = List(barCount) { RectF() }
        barPaint = Paint().apply {
            color = barColor
            isAntiAlias = true
        }

        var start = paddingLeft.toFloat()
        barPositions.forEach {
            it.left = start
            it.right = it.left + barWidth
            start = it.right + barSpace
        }

        states = DEFAULT_STATE_PROVIDER.getStates(barCount)
        currentState = ArrayList(states.first())
        animatorSet = buildAnimation()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        updatePositions()
        drawBars(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(canvasWidth.toInt(), heightMeasureSpec)
    }

    fun start() {
        if (animatorSet.isPaused) {
            animatorSet.resume()
        } else if (!animatorSet.isRunning) {
            animatorSet.apply {
                start()
                doOnEnd {
                    it.start()
                }
            }
        }
    }

    fun stop() {
        animatorSet.pause()
    }

    private fun buildAnimation(): AnimatorSet {
        val animations = states.indices.map { state ->
            val animation = (0 until barCount).map {
                val from = states[state][it]
                val to = states[(state + 1) % states.size][it]
                ObjectAnimator.ofFloat(from, to).apply {
                    interpolator = AccelerateInterpolator()
                    duration = animationDuration
                    addUpdateListener { animation ->
                        currentState[it] = animation.animatedValue as Float
                        invalidate()
                    }
                }
            }
            AnimatorSet().apply {
                playTogether(animation)
            }
        }

        return AnimatorSet().apply {
            playSequentially(animations)
        }
    }

    private fun updatePositions() {
        val barHeight = height - paddingBottom - paddingTop
        barPositions.forEachIndexed { index, it ->
            it.top = paddingTop + (1 - currentState[index]) * barHeight / 2
            it.bottom = height / 2 + currentState[index] * barHeight / 2
        }
    }

    private fun drawBars(canvas: Canvas) {
        barPositions.forEach {
            canvas.drawRect(it, barPaint)
        }
    }

    companion object {
        private fun toPixel(dpi: Int): Float {
            return Resources.getSystem().displayMetrics.density * dpi
        }

        private const val DEFAULT_BAR_COUNT = 5
        private const val DEFAULT_BAR_COLOR = Color.BLUE
        private const val DEFAULT_ANIMATION_DURATION_MS = 200
        private val DEFAULT_BAR_WIDTH = toPixel(2)
        private val DEFAULT_BAR_SPACE = toPixel(1)
        private val DEFAULT_STATE_PROVIDER = SinStateProvider
    }
}
