package com.prembros.facilis.util

import android.view.*
import com.prembros.facilis.activity.BaseCardActivity
import com.prembros.facilis.dialog.BaseBlurPopup
import org.jetbrains.anko.toast

class LongPopupClickListener private constructor(private val activity: BaseCardActivity) {

    private lateinit var baseBlurPopup: BaseBlurPopup
    private var isLongPressStarted: Boolean = false

    companion object {
        fun inside(activity: BaseCardActivity): LongPopupClickListener = LongPopupClickListener(activity)
    }

    fun withPopup(baseBlurPopup: BaseBlurPopup): LongPopupClickListener {
        this.baseBlurPopup = baseBlurPopup
        return this
    }

    fun setOn(view: View) {
        view.setOnLongClickListener {
            isLongPressStarted = true
            activity.vibrate(40)
            activity.pushPopup(baseBlurPopup)
            true
        }
        view.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    if (isLongPressStarted) {
                        activity.onBackPressed()
                        return@setOnTouchListener true
                    } else activity.toast("Long press for the popup")
                    false
                }
                else -> false
            }
        }
    }
}