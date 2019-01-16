package com.prembros.facilis.implementation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.prembros.facilis.sample.R
import com.prembros.facilis.util.*
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerViewImplementation.onReducingClick { startActivity(intentFor<RecyclerViewImplementationActivity>()) }

        scrollViewImplementation.onElevatingClick { startActivity(intentFor<SelectionActivity>()) }

        epoxyImplementation.onReducingClick { startActivity(intentFor<EpoxyImplementationActivity>()) }
    }
}
