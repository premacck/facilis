package com.prembros.facilis.app

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.prembros.facilis.sample.R
import kotlinx.android.synthetic.main.activity_selection.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity

class SelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection)
        selectFragmentStackButton.onClick { startActivity<MainActivity>() }
        selectCardStackButton.onClick { startActivity<MyActivity>() }
    }
}