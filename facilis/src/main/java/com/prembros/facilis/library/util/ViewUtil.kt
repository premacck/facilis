@file:Suppress("DEPRECATION")

package com.prembros.facilis.library.util

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.support.v4.graphics.ColorUtils
import android.support.v7.widget.CardView
import android.util.TypedValue
import android.view.Display
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import com.prembros.facilis.R
import com.prembros.facilis.library.OnSwipeTouchListener

fun getScreenSize(display: Display): IntArray {
    val size = Point()
    display.getSize(size)
    return intArrayOf(size.x, size.y)
}

fun getDp(context: Context, dp: Float): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)
}

fun View.toggleInteraction(isEnabled: Boolean) {
    this.isEnabled = isEnabled
    this.isClickable = isEnabled
}

fun CardView.moveToBackGround() {
    val objectAnimator = ObjectAnimator.ofPropertyValuesHolder(
            this,
            PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, -getDp(this.context!!, 24f)),
            PropertyValuesHolder.ofFloat(View.SCALE_X, 0.92f),
            PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.92f)
    ).prepare()
    objectAnimator.addUpdateListener {
        this.setCardBackgroundColor(
                ColorUtils.blendARGB(
                        Color.WHITE,
                        this.resources.getColor(R.color.others_color_grey),
                        it.animatedFraction
                )
        )
    }
    objectAnimator.start()
    this.elevation = 0f
    this.toggleInteraction(false)
    this.toggleInteraction(false)
}

fun CardView.moveToForeGround() {
    val objectAnimator = ObjectAnimator.ofPropertyValuesHolder(
            this,
            PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0f),
            PropertyValuesHolder.ofFloat(View.SCALE_X, 1f),
            PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f)
    ).prepare()
    objectAnimator.addUpdateListener {
        this.setCardBackgroundColor(
                ColorUtils.blendARGB(
                        this.resources.getColor(R.color.others_color_grey),
                        Color.WHITE,
                        it.animatedFraction
                )
        )
    }
    objectAnimator.start()
    this.elevation = getDp(this.context!!, 8f)
    this.toggleInteraction(true)
    this.toggleInteraction(true)
}

fun ObjectAnimator.prepare(): ObjectAnimator {
    this.duration = 200
    this.interpolator = DecelerateInterpolator()
    return this
}

//fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
//    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
//}

fun View.setSwipeDownListener(activity: Activity, dragView: View, rootView: View, backgroundLayout: ViewGroup? = null) {
    this.setOnTouchListener(object : OnSwipeTouchListener(activity, dragView, rootView, backgroundLayout) {
        override fun onSwipeDown() {
            activity.onBackPressed()
        }
    })
}