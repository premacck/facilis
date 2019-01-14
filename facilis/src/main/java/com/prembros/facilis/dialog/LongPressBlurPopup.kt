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

open class LongPressBlurPopup private constructor(builder: Builder) : LongPressPopupInterface {

    internal var baseBlurPopup: BaseBlurPopup = builder.baseBlurPopup
    internal var parentActivity: BaseCardActivity = builder.parentActivity
    internal var mTag: String = builder.tag
    internal var mViewTarget: View = builder.targetView
    internal var rootPopupView: ViewGroup? = builder.rootPopupView
    internal var mInitialPressedView: View? = null

    internal var mPopupTouchListener: PopupTouchListener? = null
    internal var mInflaterListener: PopupInflaterListener? = builder.popupInflaterListener
    internal var mLongPressReleaseClickListener: View.OnClickListener? = builder.longPressReleaseClickListener
    internal var mOnHoverListener: PopupHoverListener? = builder.hoverListener
    internal var mPopupListener: PopupStateListener? = builder.baseBlurPopup.popupStateListener

    private var mRegistered: Boolean = false
    private val mLongPressDuration: Int = builder.longPressDuration
    private var mDismissOnLongPressStop: Boolean = builder.dismissOnLongPressStop
    private var mDismissOnTouchOutside: Boolean = builder.dismissOnTouchOutside
    private var mDispatchTouchEventOnRelease: Boolean = builder.dispatchTouchEventOnRelease
    private var mCancelTouchOnDragOutsideView: Boolean = builder.cancelTouchOnDragOutsideView
    @AnimType
    private var animationType: Int = builder.baseBlurPopup.animationType

    /**
     * Show the popup manually
     */
    fun showNow() {
        if (!mRegistered) {
            register()
        }
        showBlurPopup()
    }

    /**
     * Dismiss the popup manually
     */
    fun dismissNow() {
        if (!mRegistered) {
            register()
        }
        dismissBlurPopup()
    }

    @CallSuper
    open fun register() {
        checkNotNull(mViewTarget)

        baseBlurPopup.popupTouchListener = PopupTouchListener(this)
        mPopupTouchListener = baseBlurPopup.popupTouchListener
        mViewTarget.setOnTouchListener(mPopupTouchListener)
        mRegistered = true
    }

    @CallSuper
    open fun unregister() {
        mPopupTouchListener?.stopPress(null)
        dismissBlurPopup()
        mViewTarget.setOnTouchListener(null)
        mRegistered = false
    }

    private fun showBlurPopup() {
        checkNotNull(baseBlurPopup)
        checkNotNull(parentActivity)

        if (baseBlurPopup.isVisible) baseBlurPopup.dismiss()

        parentActivity.pushPopup(baseBlurPopup)

        mInflaterListener?.onViewInflated(mTag, rootPopupView)
        mPopupListener?.onPopupShow(mTag)
    }

    private fun dismissBlurPopup() {
        if (baseBlurPopup.isVisible) baseBlurPopup.dismiss()

        mPopupListener?.onPopupDismiss(mTag)
    }

    override fun onPressStart(pressedView: View, motionEvent: MotionEvent) {
        mInitialPressedView = pressedView
    }

    override fun onPressContinue(progress: Int, motionEvent: MotionEvent) {
        if (mCancelTouchOnDragOutsideView && mInitialPressedView != null && motionEvent.isTouchInsideView(mInitialPressedView!!)) {
            mPopupTouchListener?.stopPress(motionEvent)
        }
    }

    override fun onPressStop(motionEvent: MotionEvent?) {
        mInitialPressedView = null
    }

    override fun onLongPressStart(motionEvent: MotionEvent) = showBlurPopup()

    override fun onLongPressContinue(longPressTime: Int, motionEvent: MotionEvent) {
        if (mDispatchTouchEventOnRelease || mOnHoverListener != null) {
            rootPopupView.dispatchActiveFocusToLeavesOnly(motionEvent)
        }
    }

    override fun onLongPressEnd(motionEvent: MotionEvent) {
        if (mDispatchTouchEventOnRelease && rootPopupView != null) {
            rootPopupView.dispatchClickToLeavesOnly(motionEvent)
        }
        if (mDismissOnLongPressStop) {
            dismissBlurPopup()
        }
        mInitialPressedView = null
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
                    mLongPressReleaseClickListener?.onClick(child) ?: child.performClick()
                }
            }
        }
    }

    private fun View.isInFocus(): Boolean = isPressed

    private fun View.setFocus() {
        if (!isInFocus()) {
            isPressed = true
            mOnHoverListener?.onHoverChanged(this, true)
        }
    }

    private fun View.removeFocus() {
        if (isInFocus()) {
            isPressed = false
            mOnHoverListener?.onHoverChanged(this, false)
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

        fun animationType(@AnimType animationType: Int) = apply { baseBlurPopup.animationType = animationType }

        fun build() = LongPressBlurPopup(this)
    }
}