package com.prembros.facilis.implementation

import androidx.appcompat.widget.Toolbar
import com.prembros.facilis.activity.BaseCardActivity
import com.prembros.facilis.sample.R

abstract class BaseChildActivity : BaseCardActivity() {

    protected fun initToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_left_arrow)
        }
    }
}