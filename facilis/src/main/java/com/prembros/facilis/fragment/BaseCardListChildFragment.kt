package com.prembros.facilis.fragment

import android.view.ViewGroup

abstract class BaseCardListChildFragment : BaseCardFragment() {

    override fun getBackgroundBlurLayout(): ViewGroup? = (parentFragment as? BaseCardListContainerFragment)?.backgroundBlurLayout()
}