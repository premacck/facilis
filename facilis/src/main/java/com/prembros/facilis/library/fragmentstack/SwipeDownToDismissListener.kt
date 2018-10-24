package com.prembros.facilis.library.fragmentstack

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.support.v4.view.animation.FastOutLinearInInterpolator
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import com.prembros.facilis.library.util.getDp
import com.prembros.facilis.library.util.getScreenSize
import java.util.*

abstract class SwipeDownToDismissListener constructor(
        activity: Activity,
        private val dragView: View,
        private val rootView: View,
        backgroundLayout: ViewGroup? = null
) : OnTouchListener {
    private val gestureDetector: GestureDetector
    private val screenHeight: Int
    private val screenWidth: Float
    private var backgroundLayout: View? = null
    private var backgroundView: View? = null
    private var motionOriginX: Float = 0.toFloat()
    private var motionOriginY: Float = 0.toFloat()
    private var isDragging: Boolean = false
    private val viewOriginX: Float
    private var viewOriginY: Float = 0f

    private val percentX: Float
        get() {
            var percent = 2f * (rootView.translationX - viewOriginX) / rootView.width
            if (percent > 1) {
                percent = 1f
            }
            if (percent < -1) {
                percent = -1f
            }
            return percent
        }

    private val percentY: Float
        get() {
            var percent = 2f * (rootView.translationY - viewOriginY) / rootView.height
            if (percent > 1) {
                percent = 1f
            }
            if (percent < -1) {
                percent = -1f
            }
            return percent
        }

    init {
        gestureDetector = object : GestureDetector(
                activity,
                object : SimpleOnGestureListener() {
                    override fun onDown(e: MotionEvent): Boolean {
                        return true
                    }
                }
        ) {
            override fun onTouchEvent(event: MotionEvent): Boolean {
                val result = super.onTouchEvent(event)
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> handleActionDown(event)
                    MotionEvent.ACTION_UP -> handleActionUp(event)
                    MotionEvent.ACTION_MOVE -> handleActionMove(viewOriginY + event.rawY - motionOriginY)
                }
                return result
            }
        }
        viewOriginX = rootView.translationX
        viewOriginY = rootView.translationY
        val screenSize = getScreenSize(activity.windowManager.defaultDisplay)
        screenWidth = screenSize[0] - getDp(activity, 16f)
        screenHeight = screenSize[1]
        if (backgroundLayout != null) {
            this.backgroundLayout = backgroundLayout
            this.backgroundView = backgroundLayout.getChildAt(0)
            this.backgroundLayout!!.pivotX = (screenSize[0] / 2).toFloat()
            this.backgroundLayout!!.pivotY = 0f
        }
    }

    enum class SwipeDirection {
        Left, Right, Up, Down;
    }

    enum class Quadrant {
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    private fun handleActionDown(event: MotionEvent) {
        motionOriginX = event.rawX
        motionOriginY = event.rawY
    }

    private fun handleActionUp(event: MotionEvent) {
        if (isDragging) {
            isDragging = false

            val motionCurrentX = event.rawX
            val motionCurrentY = event.rawY

            val quadrant = getQuadrant(motionOriginX, motionOriginY, motionCurrentX, motionCurrentY)
            var radian = getRadian(motionOriginX, motionOriginY, motionCurrentX, motionCurrentY)
            var degree: Double
            var direction: SwipeDirection? = null
            when (quadrant) {
                Quadrant.TOP_LEFT -> {
                    degree = Math.toDegrees(radian)
                    degree = 180 - degree
                    radian = Math.toRadians(degree)
                    direction = if (Math.cos(radian) < -0.5) {
                        SwipeDirection.Left
                    } else {
                        SwipeDirection.Up
                    }
                }
                Quadrant.TOP_RIGHT -> {
                    degree = Math.toDegrees(radian)
                    radian = Math.toRadians(degree)
                    direction = if (Math.cos(radian) < 0.5) {
                        SwipeDirection.Up
                    } else {
                        SwipeDirection.Right
                    }
                }
                Quadrant.BOTTOM_LEFT -> {
                    degree = Math.toDegrees(radian)
                    degree += 180
                    radian = Math.toRadians(degree)
                    direction = if (Math.cos(radian) < -0.5) {
                        SwipeDirection.Left
                    } else {
                        SwipeDirection.Down
                    }
                }
                Quadrant.BOTTOM_RIGHT -> {
                    degree = Math.toDegrees(radian)
                    degree = 360 - degree
                    radian = Math.toRadians(degree)
                    direction = if (Math.cos(radian) < 0.5) {
                        SwipeDirection.Down
                    } else {
                        SwipeDirection.Right
                    }
                }
            }

            val percent: Float
            percent = if (direction == SwipeDirection.Left || direction == SwipeDirection.Right) {
                percentX
            } else {
                percentY
            }

            if (Math.abs(percent) > SWIPE_THRESHOLD_PERCENT) {
                if (direction == SwipeDirection.Down) {
                    discard(event.rawY)
                    //                    Container swiped
                } else {
                    moveToOrigin(rootView)
                    moveToOrigin(dragView)
                    resetBackgroundView()
                    //                    Container moved to origin
                }
            } else {
                moveToOrigin(rootView)
                moveToOrigin(dragView)
                resetBackgroundView()
                //                    Container moved to origin
            }
        }

        motionOriginX = event.rawX
        motionOriginY = event.rawY
    }

    private fun handleActionMove(translationY: Float) {
        isDragging = true
        rootView.translationY = translationY
        dragView.translationY = translationY
        val alpha = translationY / screenHeight


        /*
        TODO: un-comment below block for scaling of backgroundLayout whole dragging
        if (backgroundLayout != null && backgroundLayout!!.scaleX <= screenWidth) {
            backgroundLayout!!.scaleX = 1 + alpha / 10
            backgroundLayout!!.scaleY = 1 + alpha / 10
        }
        */
        if (backgroundView != null) {
            backgroundView!!.alpha = 1 - alpha
        }
    }

    private fun moveToOrigin(view: View) {
        view.animate().translationX(viewOriginX)
                .translationY(viewOriginY)
                .alpha(1f)
                .setDuration(200)
                .setInterpolator(OvershootInterpolator(1.0f))
                .start()
    }

    private fun resetBackgroundView() {
        if (backgroundLayout != null) {
            backgroundLayout!!.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(200)
                    .setInterpolator(OvershootInterpolator(1.0f))
                    .start()
        }
        if (backgroundView != null) {
            backgroundView!!.animate()
                    .alpha(0f)
                    .setDuration(200)
                    .setInterpolator(OvershootInterpolator(1.0f))
                    .start()
        }
    }

    private fun discard(eventUpY: Float) {
        val discardAnimator = ValueAnimator.ofFloat(eventUpY, screenHeight.toFloat())
        discardAnimator.duration = 200
        discardAnimator.interpolator = FastOutLinearInInterpolator()
        discardAnimator.addUpdateListener { animation -> handleActionMove(animation.animatedValue as Float) }
        discardAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                onSwipeDown()
            }
        })
        discardAnimator.start()
    }

    abstract fun onSwipeDown()

    companion object {
        private val FREEDOM_NO_TOP = Arrays.asList(SwipeDirection.Down, SwipeDirection.Left, SwipeDirection.Right)!!
        private val HORIZONTAL = Arrays.asList(SwipeDirection.Left, SwipeDirection.Right)!!
        private val VERTICAL = Arrays.asList(SwipeDirection.Up, SwipeDirection.Down)!!

        fun from(value: Int): List<SwipeDirection> {
            return when (value) {
                0 -> HORIZONTAL
                1 -> VERTICAL
                else -> FREEDOM_NO_TOP
            }
        }

        private const val SWIPE_THRESHOLD_PERCENT = 0.4f

        private fun getRadian(x1: Float, y1: Float, x2: Float, y2: Float): Double {
            val width = x2 - x1
            val height = y1 - y2
            return Math.atan((Math.abs(height) / Math.abs(width)).toDouble())
        }

        private fun getQuadrant(x1: Float, y1: Float, x2: Float, y2: Float): Quadrant {
            return if (x2 > x1) { // Right
                if (y2 > y1) { // Bottom
                    Quadrant.BOTTOM_RIGHT
                } else { // Top
                    Quadrant.TOP_RIGHT
                }
            } else { // Left
                if (y2 > y1) { // Bottom
                    Quadrant.BOTTOM_LEFT
                } else { // Top
                    Quadrant.TOP_LEFT
                }
            }
        }
    }
}