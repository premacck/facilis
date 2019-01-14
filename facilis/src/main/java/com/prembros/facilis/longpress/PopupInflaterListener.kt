package com.prembros.facilis.longpress

import android.view.View

interface PopupInflaterListener {
    /**
     * Called when the popup view has been inflated (not necessarily shown, I recommend using
     * [PopupStateListener] to know when the popup is show (to load images for example))
     *
     * @param tag           The tag of the popup
     * @param rootView      The inflated rootView, you can use findViewById method on it and look for your views
     */
    fun onViewInflated(tag: String?, rootView: View?)
}