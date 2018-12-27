package com.prembros.facilis.util

import android.animation.ObjectAnimator
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.*
import com.prembros.facilis.sample.R.drawable.ic_warning
import com.squareup.picasso.*
import com.squareup.picasso.Target

fun ImageView.dynamicHeightTarget(activity: Activity?) = object : Target {
    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}

    override fun onBitmapFailed(e: Exception, errorDrawable: Drawable?) {
        Log.e("onBitmapFailed", e.message, e)
        setImageResource(ic_warning)
    }

    override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
        if (activity == null) return

        val width = (activity.windowManager.defaultDisplay.getScreenSize()[0] - activity.getDp(18f)).toInt()
        val params = this@dynamicHeightTarget.layoutParams as RelativeLayout.LayoutParams
//        val previousHeight = params.height
        params.height = width * bitmap.height / bitmap.width

//        scaleY(previousHeight.toFloat(), params.height.toFloat())

        this@dynamicHeightTarget.layoutParams = params
        setImageBitmap(bitmap)
    }
}

fun View.scaleY(from: Float, to: Float) = ObjectAnimator.ofFloat(this, View.SCALE_Y, from, to).run {
    duration = 280
    interpolator = DecelerateInterpolator(2.0f)
    start()
}