package com.prembros.facilis.longpress

import android.view.*

interface LongPressPopupInterface {

    /**
     * Called when normal press register, still NOT long press
     */
    fun onPressStart(pressedView: View, motionEvent: MotionEvent)

    /**
     * Called while continue to press, but still not for long enough
     *
     * @param progress The current progress towards the long press
     */
    fun onPressContinue(progress: Int, motionEvent: MotionEvent)

    /**
     * Called when a press event stops before reaching the long press needed time
     */
    fun onPressStop(motionEvent: MotionEvent?)

    /**
     * Called when the button has been long pressed for long enough, passing the last touch
     * coordinates
     */
    fun onLongPressStart(motionEvent: MotionEvent)

    /**
     * Called when keep long pressing
     *
     * @param longPressTime The time the view has been long clicked for
     */
    fun onLongPressContinue(longPressTime: Int, motionEvent: MotionEvent)

    /**
     * Called when stopping the long press
     */
    fun onLongPressEnd(motionEvent: MotionEvent)
}