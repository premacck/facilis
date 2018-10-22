package com.prembros.facilis.library.util

import android.content.Context
import android.graphics.Point
import android.util.TypedValue
import android.view.Display
import android.view.View

fun getScreenSize(display: Display): IntArray {
    val size = Point()
    display.getSize(size)
    return intArrayOf(size.x, size.y)
}

fun getDp(context: Context, dp: Float): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)
}

fun View.toggleViewinteraction(isEnabled: Boolean) {
    this.isEnabled = isEnabled
    this.isClickable = isEnabled
}