package com.prembros.facilis.util

import android.util.Log
import androidx.annotation.AnimRes
import androidx.fragment.app.*
import com.prembros.facilis.R
import com.prembros.facilis.activity.BaseCardActivity
import com.prembros.facilis.dialog.*
import com.prembros.facilis.fragment.*

fun FragmentManager.findCard(tag: String): BaseCardFragment? = this.findFragmentByTag(tag) as? BaseCardFragment

fun FragmentManager.findLastCard(): BaseCardFragment? = fragments[fragments.size - 1] as? BaseCardFragment

inline fun <reified T : Any> FragmentManager.findFragment(): BaseFragment? {
    for (fragment in fragments.reversed()) {
        if (fragment is T && fragment is BaseFragment) {
            return fragment
        }
    }
    return null
}

fun FragmentManager.findLastFragment(): BaseFragment? {
    for (fragment in fragments.reversed()) {
        if (fragment is BaseFragment) {
            return fragment
        }
    }
    return null
}

fun FragmentManager.findLastFragment(tag: String?): BaseFragment? {
    return findFragmentByTag(tag) as? BaseFragment
}

fun FragmentManager.findPopupDialog(tag: String): BaseBlurPopup? {
    return findFragmentByTag(tag) as? BaseBlurPopup
}

inline fun <reified T : BaseFragment> BaseCardActivity.removeFragmentIfExists() {
    for (fragment in supportFragmentManager.fragments.reversed()) {
        if (fragment is T) {
            popBackStack()
        }
    }
}

fun FragmentManager.removeActivePopupsIfAny(): Boolean {
    for (popup in fragments.reversed()) {
        if (popup is BaseDialogFragment && popup.isAdded) {
            if (popup.onBackPressed()) {
                popup.smartDismiss { popBackStack() }
            }
            return false
        }
    }
    return true
}

fun FragmentManager.removeActiveCardsIfAny(): Boolean {
    for (card in fragments.reversed()) {
        if (card is BaseCardFragment && card.isAdded) {
            return if (card.onBackPressed()) {
                popBackStack()
                false
            } else true
        }
    }
    return true
}

@Suppress("DEPRECATION")
fun FragmentManager.moveCurrentCardToBackground() {
    val lastFragment = findLastFragment()
    lastFragment?.run {
        (this as? CardContainer)?.moveToBackGround()
        onPause()
    }
}

fun FragmentManager.movePreviousCardToForeground() {
    val lastFragment = findLastFragment()
    lastFragment?.run {
        (this as? CardContainer)?.moveToForeGround()
        onResume()
    }
}

fun FragmentManager.pushFragment(resId: Int, card: BaseFragment, tag: String, index: Int, isAddToBackStack: Boolean = true) {
    if (index < 0) return
    this.beginTransaction()
            .addCustomAnimations(R.anim.float_up, R.anim.sink_up, R.anim.float_down, R.anim.sink_down, index)
            .add(resId, card, tag)
            .addToBackStack(isAddToBackStack, tag)
            .commit()
    Log.i("pushFragment", "Added index: $index")
}

fun FragmentManager.pushPopup(resId: Int, popup: BaseDialogFragment, tag: String) {
    this.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .add(resId, popup, tag)
            .addToBackStack(tag)
            .commit()
}

fun FragmentTransaction.addCustomAnimations(@AnimRes enter: Int, @AnimRes exit: Int, @AnimRes popEnter: Int, @AnimRes popExit: Int, index: Int = 0): FragmentTransaction {
    if (index > 0) this.setCustomAnimations(enter, exit, popEnter, popExit)
    return this
}

fun FragmentTransaction.addToBackStack(addFlag: Boolean = true, tag: String): FragmentTransaction {
    return if (addFlag) this.addToBackStack(tag) else this
}
