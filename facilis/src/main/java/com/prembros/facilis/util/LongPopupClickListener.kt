package com.prembros.facilis.util

import android.view.MotionEvent.*
import android.view.View
import com.prembros.facilis.activity.BaseCardActivity
import com.prembros.facilis.dialog.BaseBlurPopup
import org.jetbrains.anko.toast

class LongPopupClickListener private constructor(private val activity: BaseCardActivity) {

    private var baseBlurPopup: BaseBlurPopup? = null
    private var isLongPressStarted: Boolean = false
    private var isVibrationEnabled: Boolean = false
    private var vibrationDurationMillis: Long = DEFAULT_VIBRATION_DURATION

    companion object {
        private const val DEFAULT_VIBRATION_DURATION = 50L
        fun inside(activity: BaseCardActivity): LongPopupClickListener = LongPopupClickListener(activity)
    }

    fun withVibration(durationMillis: Long = DEFAULT_VIBRATION_DURATION): LongPopupClickListener {
        isVibrationEnabled = true
        vibrationDurationMillis = durationMillis
        return this
    }

    fun withPopup(baseBlurPopup: BaseBlurPopup?): LongPopupClickListener {
        this.baseBlurPopup = baseBlurPopup
        return this
    }

    fun setOn(view: View) {
        if (baseBlurPopup == null) throw IllegalStateException("BaseBlurPopup must be set by calling withPopup(BaseBlurPopup) before calling this method")

        view.setOnLongClickListener {
            if (!isLongPressStarted) {
                isLongPressStarted = true
                activity.vibrate(vibrationDurationMillis)
                activity.pushPopup(baseBlurPopup!!)
                return@setOnLongClickListener true
            } else isLongPressStarted = false
            false
        }
        view.setOnTouchListener { _, event ->
            when (event.action) {
                ACTION_UP, ACTION_CANCEL, ACTION_OUTSIDE -> {
                    if (isLongPressStarted) {
                        isLongPressStarted = false
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