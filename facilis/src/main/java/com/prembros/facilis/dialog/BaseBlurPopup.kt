package com.prembros.facilis.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.annotation.*
import com.prembros.facilis.R
import com.prembros.facilis.activity.BaseCardActivity
import com.prembros.facilis.dialog.LongPressBlurPopup.AnimType
import com.prembros.facilis.dialog.LongPressBlurPopup.AnimType.Companion.ANIM_FROM_BOTTOM
import com.prembros.facilis.fragment.BaseFragment
import com.prembros.facilis.longpress.*
import com.prembros.facilis.util.*
import io.alterac.blurkit.BlurLayout
import org.jetbrains.anko.sdk27.coroutines.onClick

fun BaseBlurPopup.withEnterAnim(@AnimRes enterAnim: Int): BaseBlurPopup {
    enterAnimRes = enterAnim
    return this
}

fun BaseBlurPopup.withExitAnim(@AnimRes exitAnim: Int): BaseBlurPopup {
    exitAnimRes = exitAnim
    return this
}

@Suppress("DeferredResultUnused")
abstract class BaseBlurPopup : BaseDialogFragment() {

    internal var container: ViewGroup? = null
    var popupTag: String? = null
    internal var isLongPressPopup: Boolean = false
    internal var popupStateListener: PopupStateListener? = null
    internal var popupTouchListener: PopupTouchListener? = null
    var overrideWindowAnimations: Boolean = false
    @AnimRes
    internal var enterAnimRes: Int = R.anim.float_up
    @AnimRes
    internal var exitAnimRes: Int = R.anim.sink_down
    @AnimType
    internal var animationType: Int = ANIM_FROM_BOTTOM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resolveEnterExitAnim(animationType)
    }

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.container = container
        return super.onCreateView(inflater, container, savedInstanceState)
    }

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

    fun getParentActivity(): BaseCardActivity = activity as? BaseCardActivity
            ?: throw IllegalStateException("parent activity of this popup must be a BaseCardActivity")

    @CallSuper
    override fun dismiss() {
        getRootView()?.hideSoftKeyboard()
        getBlurLayout()?.fadeOut()
        getRootView()?.animation?.run {}
                ?: getRootView()?.animate(dismissAnimation())?.then { super.dismiss() }
                ?: super.dismiss()
    }

    override fun onDismiss(dialog: DialogInterface?) {
        popupStateListener?.onPopupDismiss(popupTag)
        popupTouchListener?.stopPress(null)
        super.onDismiss(dialog)
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