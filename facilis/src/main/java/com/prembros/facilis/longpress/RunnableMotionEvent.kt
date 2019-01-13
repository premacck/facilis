package com.prembros.facilis.longpress

import android.view.MotionEvent
import android.view.View

abstract class RunnableMotionEvent : Runnable {
    var lastMotionEvent: MotionEvent? = null
    var startView: View? = null
}
