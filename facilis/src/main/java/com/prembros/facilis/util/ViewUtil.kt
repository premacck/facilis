package com.prembros.facilis.util

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.os.*
import android.util.TypedValue
import android.view.*
import android.view.MotionEvent.*
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.ColorRes
import androidx.fragment.app.Fragment
import com.prembros.facilis.R
import com.prembros.facilis.swiper.SwipeListener
import kotlinx.coroutines.*
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.sdk27.coroutines.*

fun Context.getDp(dp: Float): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)

fun View.getDp(dp: Float): Float = context.getDp(dp)

@Suppress("DEPRECATION")
fun Context.colorOf(@ColorRes color: Int): Int = resources.getColor(color)

fun Fragment.colorOf(@ColorRes color: Int) = context?.colorOf(color)!!

fun View.colorOf(@ColorRes color: Int) = context?.colorOf(color)!!

fun Display.getScreenSize(): IntArray {
    val size = Point()
    getSize(size)
    return intArrayOf(size.x, size.y)
}

fun View.makeVisible() {
    if (visibility != View.VISIBLE) visibility = View.VISIBLE
}

fun View.makeInvisible() {
    if (visibility != View.INVISIBLE) visibility = View.INVISIBLE
}

fun View.makeGone() {
    if (visibility != View.GONE) visibility = View.GONE
}

fun <T : View> Array<T>.makeVisible() = forEach { if (it.visibility != View.VISIBLE) it.visibility = View.VISIBLE }

fun <T : View> Array<T>.makeInvisible() = forEach { if (it.visibility != View.INVISIBLE) it.visibility = View.INVISIBLE }

fun <T : View> Array<T>.makeGone() = forEach { if (it.visibility != View.GONE) it.visibility = View.GONE }

fun View.toggleInteraction(isEnabled: Boolean) {
    this.isEnabled = isEnabled
    this.isClickable = isEnabled
}

fun View.setSwipeDownListener(activity: Activity, rootView: View?, backgroundLayout: ViewGroup? = null) =
        onSwipeDown(activity, rootView, backgroundLayout, { activity.onBackPressed() })

fun View.onSwipeDown(activity: Activity, rootView: View? = null, backgroundLayout: ViewGroup? = null, swipeDownAction: () -> Unit, singleTapAction: () -> Boolean = { false }) =
        setSwipeListener(activity, rootView, backgroundLayout, swipeDownAction, singleTapAction, {}, {}, {})

fun View.setSwipeListener(
        activity: Activity,
        rootView: View? = null,
        backgroundLayout: ViewGroup? = null,
        swipeDownAction: () -> Unit,
        singleTapAction: () -> Boolean,
        swipeUpAction: () -> Unit,
        swipeLeftAction: () -> Unit,
        swipeRightAction: () -> Unit
) {
    setOnTouchListener(object : SwipeListener(
            activity,
            this@setSwipeListener,
            rootView ?: this@setSwipeListener,
            backgroundLayout
    ) {
        override fun onSwipeDown() = swipeDownAction()
        override fun onSingleTap(): Boolean = singleTapAction()
        override fun onSwipeUp() = swipeUpAction()
        override fun onSwipeLeft() = swipeLeftAction()
        override fun onSwipeRight() = swipeRightAction()
    })
}

fun View.setMargin(left: Int? = null, top: Int? = null, right: Int? = null, bottom: Int? = null) {
    layoutParams = (layoutParams as? ViewGroup.MarginLayoutParams)?.apply {
        left?.let { leftMargin = it; }
        top?.let { topMargin = it }
        right?.let { rightMargin = it }
        bottom?.let { bottomMargin = it }
    }
}

fun Context.vibrate(millis: Long) {
    val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(VibrationEffect.createOneShot(millis, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        @Suppress("DEPRECATION")
        vibrator.vibrate(millis)
    }
}

fun View.vibrate(millis: Long) = context.vibrate(millis)

fun View.longClickWithVibrate(action: () -> Unit) {
    setOnLongClickListener {
        vibrate(20)
        action()
        return@setOnLongClickListener true
    }
}

fun View.onCustomLongClick(longClickDelay: Int = 300, action: () -> Unit) = setOnTouchListener(getCustomOnLongClickListener(longClickDelay) { action() })

fun View.onDebouncingClick(action: () -> Unit) {
    setOnClickListener {
        if (isEnabled) {
            isEnabled = false
            action()
            postDelayed({ isEnabled = true }, 100)
        }
    }
}

fun View.onReducingClick(launchDelay: Long = 100, action: () -> Unit) {
    lateinit var originPoint: ArrayList<Float>
    onTouch { _, event ->
        when (event.action) {
            ACTION_DOWN -> {
                originPoint = arrayListOf(event.rawX, event.rawY)
                animatorOf(R.animator.reduce_size).start()
            }
            ACTION_CANCEL, ACTION_UP -> animatorOf(R.animator.original_size).start()
            ACTION_MOVE -> {
                if (isNullOrEmpty(originPoint)) originPoint = arrayListOf(event.rawX, event.rawY)

                if (!isNullOrEmpty(originPoint)) {
                    val deltaX = Math.abs(originPoint[0] - event.rawX)
                    val deltaY = Math.abs(originPoint[1] - event.rawY)
                    if (deltaX > 1 && deltaY > 1) {
                        animatorOf(R.animator.original_size).start()
                    }
                }
            }
        }
    }
    onDebouncingClick { this.context.doAfterDelay(launchDelay) { action() } }
}

fun View.onElevatingClick(launchDelay: Long = 100, elevateBy: Float = 4f, action: () -> Unit) {
    lateinit var originPoint: ArrayList<Float>
    val originalElevation = elevation
    val finalElevation = elevation + getDp(elevateBy)
    val elevateUpAnimation = ValueAnimator.ofFloat(originalElevation, finalElevation).setDuration(80)
    elevateUpAnimation.interpolator = DecelerateInterpolator()
    elevateUpAnimation.addUpdateListener { elevation = it.animatedValue as Float }

    val elevateDownAnimation = ValueAnimator.ofFloat(finalElevation, originalElevation).setDuration(100)
    elevateDownAnimation.addUpdateListener { elevation = it.animatedValue as Float }
    elevateDownAnimation.interpolator = DecelerateInterpolator()
    elevateDownAnimation.startDelay = 80
    onTouch { _, event ->
        when (event.action) {
            ACTION_DOWN -> {
                originPoint = arrayListOf(event.rawX, event.rawY)
                elevateUpAnimation.start()
            }
            ACTION_CANCEL, ACTION_UP -> elevateDownAnimation.start()
            ACTION_MOVE -> {
                if (isNullOrEmpty(originPoint)) originPoint = arrayListOf(event.rawX, event.rawY)

                if (!isNullOrEmpty(originPoint)) {
                    val deltaX = Math.abs(originPoint[0] - event.rawX)
                    val deltaY = Math.abs(originPoint[1] - event.rawY)
                    if (deltaX > 1 && deltaY > 1) {
                        elevateDownAnimation.start()
                    }
                }
            }
        }
    }
    onDebouncingClick { this.context.doAfterDelay(launchDelay) { action() } }
}

fun View.clearOnClickListener() = setOnClickListener(null)

private fun View.getCustomOnLongClickListener(longClickDelay: Int = 300, action: () -> Unit): View.OnTouchListener {
    return object : View.OnTouchListener {
        private var isLongPress = false
        private lateinit var originPoint: FloatArray
        private lateinit var latestPoint: FloatArray
        private var downTime: Long = 0

        private fun arePointsWithinBounds(): Boolean {
            val deltaX = Math.abs(originPoint[0] - latestPoint[0])
            val deltaY = Math.abs(originPoint[1] - latestPoint[1])
            return deltaX <= 15 && deltaY <= 15
        }

        private fun isTimeWithinBounds(): Boolean {
            return Math.abs(downTime - SystemClock.elapsedRealtime()) <= longClickDelay
        }

        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    downTime = SystemClock.elapsedRealtime()
                    originPoint = floatArrayOf(event.rawX, event.rawY)
                    latestPoint = originPoint
                    isLongPress = true
                    this@getCustomOnLongClickListener.postDelayed({
                        if (isLongPress && arePointsWithinBounds()) {
                            isLongPress = false
                            vibrate(20)
                            action()
                        }
                    }, longClickDelay.toLong())
                }
                MotionEvent.ACTION_UP -> {
                    if (isLongPress && arePointsWithinBounds() && isTimeWithinBounds()) {
                        isLongPress = false
                        performClick()
                        return false
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    latestPoint = floatArrayOf(event.rawX, event.rawY)
                }
            }
            return true
        }
    }
}

inline fun <reified T : View> Array<T>.onClick(crossinline action: (view: View) -> Unit) {
    for (view in this) {
        view.onDebouncingClick { action(view) }
    }
}

inline fun <reified T : View> Array<T>.onTextChanged(crossinline action: () -> Unit) {
    for (view in this) {
        if (view is EditText) {
            view.textChangedListener { onTextChanged { _, _, _, _ -> action() } }
        }
    }
}

fun Fragment.doAfterDelay(delayMillis: Long, action: () -> Unit) = context?.doAfterDelay(delayMillis, action)

@Suppress("DeferredResultUnused")
fun Context?.doAfterDelay(delayMillis: Long, action: () -> Unit) {
    GlobalScope.async {
        delay(delayMillis)
        this@doAfterDelay?.run { runOnUiThread { action() } }
    }
}

inline fun <reified T : View> ViewGroup.getIfPresent(): T? {
    if (childCount <= 0) {
        return null
    }
    for (i in 0..childCount) {
        val child = getChildAt(i)
        if (child is T) {
            return child
        }
    }
    return null
}

fun View.hideSoftKeyboard() = (context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.hideSoftInputFromWindow(windowToken, 0)

fun View.showSoftKeyboard() = (context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
