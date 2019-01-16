package com.prembros.facilis.implementation

import android.os.Bundle
import com.prembros.facilis.activity.BaseCardActivity
import com.prembros.facilis.implementation.adapter.CardsAdapter
import com.prembros.facilis.sample.R
import kotlinx.android.synthetic.main.activity_recycler_view_implementation.*

class RecyclerViewImplementationActivity : BaseCardActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view_implementation)
        recyclerView.adapter = CardsAdapter(this)
    }

    override fun getFragmentContainer(): Int = R.id.fragmentContainer
}