package com.prembros.facilis.fragment

import android.view.ViewGroup
import com.prembros.facilis.activity.BaseCardActivity

interface CardContainer {

    fun getRootView(): ViewGroup?

    fun moveToBackGround()

    fun moveToForeGround()

    fun parentActivity(): BaseCardActivity
}