package com.prembros.facilis.implementation

import android.os.Bundle
import com.prembros.facilis.activity.BaseCardActivity
import com.prembros.facilis.implementation.adapter.CardsAdapter
import com.prembros.facilis.implementation.controller.CardsController
import com.prembros.facilis.sample.R
import com.prembros.facilis.util.tileList
import kotlinx.android.synthetic.main.activity_epoxy_implementation.*

class EpoxyImplementationActivity : BaseChildActivity() {

    private lateinit var cardsController: CardsController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_epoxy_implementation)
        initToolbar(toolbar)

        cardsController = CardsController(this)
        recyclerView.setController(cardsController)
        cardsController.setData(tileList)
    }

    override fun getFragmentContainer(): Int = R.id.fragmentContainer
}