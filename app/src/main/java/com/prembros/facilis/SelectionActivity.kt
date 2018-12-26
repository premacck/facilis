package com.prembros.facilis

import android.os.Bundle
import com.prembros.facilis.activity.BaseCardActivity
import com.prembros.facilis.sample.R
import com.prembros.facilis.util.*
import kotlinx.android.synthetic.main.activity_selection.*

class SelectionActivity : BaseCardActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection)

        cardWithListBtn.onReducingClick {
            //            TODO: launch card with RecyclerView
        }

        listOfCardsBtn.onReducingClick {
            //            TODO: launch list of cards
        }

        plainCardBtn.onElevatingClick { pushFragment(PlainCardFragment()) }

        blurPopupBtn.onDebouncingClick {
            //            TODO: push blurPopup
        }

        longPressBlurPopupBtn.onDebouncingClick {
            //            TODO: push long-press blur popup which gets dismissed on [ACTION_UP]
        }
    }

    override fun getFragmentContainer(): Int = R.id.fragmentContainer
}