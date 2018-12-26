package com.prembros.facilis.dialog

import android.view.*
import androidx.annotation.*
import com.prembros.facilis.R
import com.prembros.facilis.activity.BaseCardActivity
import com.prembros.facilis.fragment.BaseFragment
import com.prembros.facilis.util.*
import io.alterac.blurkit.BlurLayout
import org.jetbrains.anko.sdk27.coroutines.onClick

@Suppress("DeferredResultUnused")
abstract class BaseBlurPopup : BaseDialogFragment() {

    override fun onStart() {
        super.onStart()
        getRootView()?.visibility = View.INVISIBLE
        doAfterDelay(10) {
            activity?.runOnUiThread {
                getBlurLayout()?.beginBlur()
                getRootView()?.run {
                    animate(enterAnimation()).then {
                        visibility = View.VISIBLE
                        isClickable = true
                    }
                }
                setupSwipeDownToCloseGesture()
                doOnStart()
            }
        }
        getBlurLayout()?.onClick { activity?.onBackPressed() }
    }

    override fun onStop() {
        getBlurLayout()?.pauseBlur()
        doOnStop()
        super.onStop()
    }

    private fun setupSwipeDownToCloseGesture() = getDragHandle()?.setSwipeDownListener(activity!!, getRootView()!!, getBlurLayout())

    fun pushFragment(baseFragment: BaseFragment, isAddToBackStack: Boolean = true) {
        getParentActivity().pushFragment(baseFragment, isAddToBackStack)
    }

    protected fun getParentActivity(): BaseCardActivity = activity as? BaseCardActivity
            ?: throw IllegalStateException("parent activity of this popup must be a BaseCardActivity")

    @CallSuper
    override fun dismiss() {
        getRootView()?.hideSoftKeyboard()
        getBlurLayout()?.fadeOut()
        getRootView()?.animation?.run {}
                ?: getRootView()?.animate(dismissAnimation())?.then { super.dismiss() }
                ?: super.dismiss()
    }

    /**
     * Method for initializing items of the popup AFTER the onStart() is called and the transitions are finished
     */
    open fun doOnStart() {}

    open fun doOnStop() {}

    @AnimRes
    open fun enterAnimation(): Int = R.anim.float_up

    @AnimRes
    open fun dismissAnimation(): Int = R.anim.sink_down

    abstract fun getBlurLayout(): BlurLayout?

    abstract fun getDragHandle(): View?

    abstract fun getRootView(): View?

    abstract fun getBackgroundLayout(): ViewGroup?
}