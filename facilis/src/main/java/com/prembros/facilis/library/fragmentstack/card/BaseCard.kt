package com.prembros.facilis.library.fragmentstack.card

import android.app.Activity
import android.support.v4.app.Fragment

abstract class BaseCard : Fragment() {

    protected fun setup(activity: Activity?, index: Int) {
        adjustWithIndex(index)
        activity?.let { setupSwipeDownGesture(it) }
    }

    abstract fun adjustWithIndex(index: Int)

    abstract fun setupSwipeDownGesture(activity: Activity)

    abstract fun moveToBackGround()

    abstract fun moveToForeGround()

    abstract fun dispose()

    abstract fun getParentActivity(): Activity

    override fun onDestroyView() {
        dispose()
        super.onDestroyView()
    }
}