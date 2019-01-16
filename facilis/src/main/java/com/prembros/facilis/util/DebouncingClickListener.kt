package com.prembros.facilis.util

import android.view.View

/**
 * A click listener [View.OnClickListener] that debounces multiple clicks posted in the
 * same frame. A click on one button disables all buttons for that frame.
 * Courtesy: Jake Wharton's ButterKnife's DebouncingClickListener
 * @see [https://github.com/JakeWharton/butterknife/blob/master/butterknife-runtime/src/main/java/butterknife/internal/DebouncingClickListener.java]
 */
interface DebouncingClickListener : View.OnClickListener {

    companion object {
        internal var enabled = true

        private val ENABLE_AGAIN = { enabled = true }
    }

    override fun onClick(v: View) {
        if (enabled) {
            enabled = false
            v.post(ENABLE_AGAIN)
            onDebouncingClick(v)
        }
    }

    fun onDebouncingClick(view: View)
}