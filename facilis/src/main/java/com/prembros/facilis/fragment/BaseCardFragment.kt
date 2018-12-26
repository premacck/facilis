package com.prembros.facilis.fragment

import android.app.Activity
import android.os.Bundle
import android.view.*
import androidx.annotation.*
import com.prembros.facilis.activity.BaseCardActivity
import com.prembros.facilis.dialog.BaseDialogFragment
import com.prembros.facilis.util.*
import io.alterac.blurkit.BlurLayout
import org.jetbrains.anko.support.v4.find

abstract class BaseCardFragment : BaseFragment(), CardContainer {

    var isInForeGround: Boolean = false

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getRootView()?.run {
            setMargin(top = context.getDp(if (parentActivity().index > 0) 20f else 0f).toInt())
            isClickable = true
        }
        activity?.let { setupSwipeDownGesture(it) }
        (getBackgroundBlurLayout() as? BlurLayout)?.beginBlur()
        isInForeGround = true
    }

    private fun setupSwipeDownGesture(activity: Activity) = getDragView()?.setSwipeDownListener(activity, getRootView(), getBackgroundBlurLayout())

    @CallSuper
    override fun moveToBackGround() {
        getRootView()?.moveToBackGround(parentActivity().index)
        find<View>(dragHandleId()).fadeOut()
        isInForeGround = false
    }

    @CallSuper
    override fun moveToForeGround() {
        getRootView()?.moveToForeGround()
        find<View>(dragHandleId()).fadeIn()
        isInForeGround = true
    }

    @CallSuper
    open fun dispose() {
        getDragView()?.setOnTouchListener(null)
    }

    open fun pushFragment(baseFragment: BaseFragment, isAddToBackStack: Boolean = true) = parentActivity().pushFragment(baseFragment, isAddToBackStack)

    open fun pushPopup(dialogFragment: BaseDialogFragment) = parentActivity().pushPopup(dialogFragment)

    abstract fun getBackgroundBlurLayout(): ViewGroup?

    abstract fun getDragView(): View?

    @IdRes
    abstract fun dragHandleId(): Int

    override fun parentActivity(): BaseCardActivity = (activity as? BaseCardActivity)
            ?: throw IllegalStateException("Fragment must be attached to a BaseCardActivity")

    override fun onDestroyView() {
        getRootView()?.hideSoftKeyboard()
        (getBackgroundBlurLayout() as? BlurLayout)?.pauseBlur()
        dispose()
        super.onDestroyView()
    }
}