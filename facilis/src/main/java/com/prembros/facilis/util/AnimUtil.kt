package com.prembros.facilis.util

import android.animation.*
import android.content.Context
import android.view.*
import android.view.animation.*
import androidx.annotation.*
import com.prembros.facilis.R
import io.alterac.blurkit.BlurLayout

fun View.fadeIn(duration: Long = 280) = animate().alpha(1f).setDuration(duration).start()

fun View.fadeOut(duration: Long = 280) = animate().alpha(0f).setDuration(duration).start()

fun View.floatDown(delayMillis: Long = 50) = animate(R.anim.float_down, delayMillis).then { visibility = View.VISIBLE }

fun View.floatUp(delayMillis: Long = 20) = animate(R.anim.float_up, delayMillis).then { visibility = View.VISIBLE }

fun View.sinkDown() = animate(R.anim.sink_down).then { visibility = View.INVISIBLE }

fun View.sinkUp() = animate(R.anim.sink_up).then { visibility = View.INVISIBLE }

fun Context.animatorOf(@AnimatorRes animatorRes: Int): Animator = AnimatorInflater.loadAnimator(this, animatorRes)

fun View.zoomOut() = animate(R.anim.zoom_out)

fun View.moveToBackGround(index: Int) {
    translateScaleAnimation(-getDp(if (index > 0) 44f else 24f), 0.92f).prepare().start()
    elevation = 0f
    toggleInteraction(false)
    toggleInteraction(false)
}

fun View.moveToForeGround() {
    translateScaleAnimation(0f, 1f).prepare().start()
    elevation = getDp(8f)
    toggleInteraction(true)
    toggleInteraction(true)
}

fun View.translateScaleAnimation(translateValue: Float, scaleValue: Float): ObjectAnimator {
    return ObjectAnimator.ofPropertyValuesHolder(
            this,
            PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, translateValue),
            PropertyValuesHolder.ofFloat(View.SCALE_X, scaleValue),
            PropertyValuesHolder.ofFloat(View.SCALE_Y, scaleValue)
    )
}

fun ObjectAnimator.prepare(): ObjectAnimator {
    this.duration = 200
    this.interpolator = DecelerateInterpolator()
    return this
}

fun ViewPropertyAnimator.listener(onAnimationEnd: () -> Unit): ViewPropertyAnimator {
    return this.setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) = onAnimationEnd()
    })
}

fun View.animate(@AnimRes anim: Int, delayMillis: Long = 0): Animation {
    val animation = AnimationUtils.loadAnimation(context, anim)
    postDelayed({ startAnimation(animation) }, delayMillis)
    return animation
}

fun Animation.then(action: (animation: Animation) -> Unit): Animation {
    setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationRepeat(animation: Animation) {}
        override fun onAnimationStart(animation: Animation) {}
        override fun onAnimationEnd(animation: Animation) {
            action(animation)
        }
    })
    return this
}

fun View.animatorOf(@AnimatorRes animatorRes: Int): Animator {
    val animator = context.animatorOf(animatorRes)
    animator.setTarget(this)
    return animator
}

fun BlurLayout.beginBlur() {
    postDelayed({
        startBlur()
        visibility = View.VISIBLE
    }, 10)
}