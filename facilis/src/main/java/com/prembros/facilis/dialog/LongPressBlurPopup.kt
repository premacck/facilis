package com.prembros.facilis.dialog

import android.view.*
import androidx.annotation.*
import com.prembros.facilis.activity.BaseCardActivity
import com.prembros.facilis.dialog.LongPressBlurPopup.AnimType.Companion.ANIM_FROM_BOTTOM
import com.prembros.facilis.dialog.LongPressBlurPopup.AnimType.Companion.ANIM_FROM_LEFT
import com.prembros.facilis.dialog.LongPressBlurPopup.AnimType.Companion.ANIM_FROM_RIGHT
import com.prembros.facilis.dialog.LongPressBlurPopup.AnimType.Companion.ANIM_FROM_TOP
import com.prembros.facilis.longpress.*
import com.prembros.facilis.longpress.PopupTouchListener.Companion.DEFAULT_LONG_PRESS_DURATION
import com.prembros.facilis.util.isTouchInsideView

@Suppress("MemberVisibilityCanBePrivate", "unused")
open class LongPressBlurPopup private constructor(builder: Builder) : LongPressPopupInterface {

    internal val baseBlurPopup: BaseBlurPopup = builder.baseBlurPopup
    internal val parentActivity: BaseCardActivity = builder.parentActivity
    internal val popupTag: String = builder.tag
    internal val targetView: View = builder.targetView
    internal val rootPopupView: ViewGroup? = builder.rootPopupView
    internal var initialPressedView: View? = null

    internal val popupInflaterListener: PopupInflaterListener? = builder.popupInflaterListener
    internal val longPressReleaseClickListener: View.OnClickListener? = builder.longPressReleaseClickListener
    internal val popupHoverListener: PopupHoverListener? = builder.hoverListener
    internal val popupStateListener: PopupStateListener? = builder.baseBlurPopup.popupStateListener
    internal var popupTouchListener: PopupTouchListener? = null

    private val longPressDuration: Int = builder.longPressDuration
    private val isDismissOnLongPressStop: Boolean = builder.dismissOnLongPressStop
    private val isDismissOnTouchOutside: Boolean = builder.dismissOnTouchOutside
    private val isDispatchTouchEventOnRelease: Boolean = builder.dispatchTouchEventOnRelease
    private val isCancelTouchOnDragOutsideView: Boolean = builder.cancelTouchOnDragOutsideView
    private var isRegistered: Boolean = false
    @AnimType
    private val animationType: Int = builder.animationType

    /**
     * Show the popup manually
     */
    fun showNow() {
        if (!isRegistered) {
            register()
        }
        showBlurPopup()
    }

    /**
     * Dismiss the popup manually
     */
    fun dismissNow() {
        if (!isRegistered) {
            register()
        }
        dismissBlurPopup()
    }

    @CallSuper
    open fun register() {
        checkNotNull(targetView)

        baseBlurPopup.popupTouchListener = PopupTouchListener(this, longPressDuration)
        popupTouchListener = baseBlurPopup.popupTouchListener
        targetView.setOnTouchListener(popupTouchListener)
        isRegistered = true
    }

    @CallSuper
    open fun unregister() {
        popupTouchListener?.stopPress(null)
        dismissBlurPopup()
        targetView.setOnTouchListener(null)
        isRegistered = false
    }

    private fun showBlurPopup() {
        checkNotNull(baseBlurPopup)
        checkNotNull(parentActivity)

        if (baseBlurPopup.isVisible) baseBlurPopup.dismiss()

        parentActivity.pushPopup(baseBlurPopup.withAnimType(animationType).setDismissOnTouchOutside(isDismissOnTouchOutside))

        popupInflaterListener?.onViewInflated(popupTag, rootPopupView)
        popupStateListener?.onPopupShow(popupTag)
    }

    private fun dismissBlurPopup() {
        if (baseBlurPopup.isVisible) baseBlurPopup.dismiss()

        popupStateListener?.onPopupDismiss(popupTag)
    }

    override fun onPressStart(pressedView: View, motionEvent: MotionEvent) {
        initialPressedView = pressedView
    }

    override fun onPressContinue(progress: Int, motionEvent: MotionEvent) {
        if (isCancelTouchOnDragOutsideView && initialPressedView != null && motionEvent.isTouchInsideView(initialPressedView!!)) {
            popupTouchListener?.stopPress(motionEvent)
        }
    }

    override fun onPressStop(motionEvent: MotionEvent?) {
        initialPressedView = null
    }

    override fun onLongPressStart(motionEvent: MotionEvent) = showBlurPopup()

    override fun onLongPressContinue(longPressTime: Int, motionEvent: MotionEvent) {
        if (isDispatchTouchEventOnRelease || popupHoverListener != null) {
            rootPopupView.dispatchActiveFocusToLeavesOnly(motionEvent)
        }
    }

    override fun onLongPressEnd(motionEvent: MotionEvent) {
        if (isDispatchTouchEventOnRelease && rootPopupView != null) {
            rootPopupView.dispatchClickToLeavesOnly(motionEvent)
        }
        if (isDismissOnLongPressStop) {
            dismissBlurPopup()
        }
        initialPressedView = null
    }

    private fun ViewGroup?.dispatchActiveFocusToLeavesOnly(motionEvent: MotionEvent) {
        if (this == null || childCount == 0) return

        for (i in 0..childCount) {
            val child = getChildAt(i)
            if (child is ViewGroup) {
                child.dispatchActiveFocusToLeavesOnly(motionEvent)
            } else {
                if (motionEvent.isTouchInsideView(child)) {
                    child.setFocus()
                } else {
                    child.removeFocus()
                }
            }
        }
    }

    private fun ViewGroup?.dispatchClickToLeavesOnly(motionEvent: MotionEvent) {
        if (this == null || childCount == 0) return

        for (i in 0..childCount) {
            val child = getChildAt(i)
            if (child is ViewGroup) {
                child.dispatchClickToLeavesOnly(motionEvent)
            } else {
                if (motionEvent.isTouchInsideView(child)) {
                    longPressReleaseClickListener?.onClick(child) ?: child.performClick()
                }
            }
        }
    }

    private fun View.isInFocus(): Boolean = isPressed

    private fun View.setFocus() {
        if (!isInFocus()) {
            isPressed = true
            popupHoverListener?.onHoverChanged(this, true)
        }
    }

    private fun View.removeFocus() {
        if (isInFocus()) {
            isPressed = false
            popupHoverListener?.onHoverChanged(this, false)
        }
    }

    @kotlin.annotation.Retention
    @IntDef(ANIM_FROM_LEFT, ANIM_FROM_RIGHT, ANIM_FROM_TOP, ANIM_FROM_BOTTOM)
    annotation class AnimType {
        companion object {
            const val ANIM_FROM_LEFT = 0
            const val ANIM_FROM_RIGHT = 1
            const val ANIM_FROM_TOP = 2
            const val ANIM_FROM_BOTTOM = 3
        }
    }

    class Builder private constructor() {
        internal lateinit var baseBlurPopup: BaseBlurPopup
        internal lateinit var parentActivity: BaseCardActivity
        internal lateinit var tag: String
        internal lateinit var targetView: View
        internal var rootPopupView: ViewGroup? = null
        internal var popupInflaterListener: PopupInflaterListener? = null
        internal var longPressReleaseClickListener: View.OnClickListener? = null
        internal var hoverListener: PopupHoverListener? = null
        internal var longPressDuration: Int = DEFAULT_LONG_PRESS_DURATION
        internal var dismissOnLongPressStop: Boolean = true
        internal var dismissOnTouchOutside: Boolean = true
        internal var dispatchTouchEventOnRelease: Boolean = true
        internal var cancelTouchOnDragOutsideView: Boolean = true
        @AnimType
        internal var animationType: Int = ANIM_FROM_BOTTOM

        companion object {
            fun with(parentActivity: BaseCardActivity) = Builder().apply { this.parentActivity = parentActivity }
        }

        fun baseBlurPopup(baseBlurPopup: BaseBlurPopup) = apply {
            this.baseBlurPopup = baseBlurPopup.apply { isLongPressPopup = true }
            rootPopupView = baseBlurPopup.container
            tag = baseBlurPopup.popupTag ?: "${baseBlurPopup::class.java.simpleName}${System.currentTimeMillis()}"
        }

        fun targetView(targetView: View) = apply { this.targetView = targetView }

        fun popupInflaterListener(popupInflaterListener: PopupInflaterListener) = apply { this.popupInflaterListener = popupInflaterListener }

        fun longPressReleaseClickListener(longPressReleaseClickListener: View.OnClickListener) = apply { this.longPressReleaseClickListener = longPressReleaseClickListener }

        fun hoverListener(hoverListener: PopupHoverListener) = apply { this.hoverListener = hoverListener }

        fun popupStateListener(popupStateListener: PopupStateListener) = apply { baseBlurPopup.popupStateListener = popupStateListener }

        fun longPressDuration(longPressDuration: Int) = apply { this.longPressDuration = longPressDuration }

        fun dismissOnLongPressStop(dismissOnLongPressStop: Boolean) = apply { this.dismissOnLongPressStop = dismissOnLongPressStop }

        fun dismissOnTouchOutside(dismissOnTouchOutside: Boolean) = apply { this.dismissOnTouchOutside = dismissOnTouchOutside }

        fun dispatchTouchEventOnRelease(dispatchTouchEventOnRelease: Boolean) = apply { this.dispatchTouchEventOnRelease = dispatchTouchEventOnRelease }

        fun cancelTouchOnDragOutsideView(cancelTouchOnDragOutsideView: Boolean) = apply { this.cancelTouchOnDragOutsideView = cancelTouchOnDragOutsideView }

        fun animationType(@AnimType animationType: Int) = apply { this.animationType = animationType }

        fun build() = LongPressBlurPopup(this)
    }
}