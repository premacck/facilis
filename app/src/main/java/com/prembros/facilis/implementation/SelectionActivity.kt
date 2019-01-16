package com.prembros.facilis.implementation

import android.os.Bundle
import com.prembros.facilis.activity.BaseCardActivity
import com.prembros.facilis.blurpopup.SampleBlurPopup
import com.prembros.facilis.cardwithlist.CardWithListFragment
import com.prembros.facilis.dialog.*
import com.prembros.facilis.listofcards.ListOfCardsContainerFragment
import com.prembros.facilis.plaincard.PlainCardFragment
import com.prembros.facilis.sample.R
import com.prembros.facilis.util.*
import kotlinx.android.synthetic.main.activity_selection.*

class SelectionActivity : BaseChildActivity() {

    private var longPressBlurPopup: LongPressBlurPopup? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection)
        initToolbar(toolbar)

        cardWithListBtn.onReducingClick { pushFragment(CardWithListFragment.newInstance(true)) }

        listOfCardsBtn.onReducingClick { pushFragment(ListOfCardsContainerFragment()) }

        plainCardBtn.onElevatingClick { pushFragment(PlainCardFragment()) }

        blurPopupFloatUpBtn.onDebouncingClick { pushPopup(SampleBlurPopup.newInstance(R.anim.float_up, R.anim.sink_down)) }

        blurPopupZoomBtn.onDebouncingClick { pushPopup(SampleBlurPopup.newInstance(R.anim.zoom_in, R.anim.zoom_out)) }

        longPressBlurPopup.checkAndUnregister()

        longPressBlurPopup = LongPressBlurPopup.Builder
                .with(this)
                .targetView(longPressBlurPopupBtn)
                .baseBlurPopup(SampleBlurPopup.newInstance())
                .longPressDuration(500)
                .animationType(AnimType.ANIM_FROM_CENTER)
                .build()
    }

    override fun onStart() {
        super.onStart()
        longPressBlurPopup?.register()
    }

    override fun onStop() {
        longPressBlurPopup?.unregister()
        super.onStop()
    }

    override fun getFragmentContainer(): Int = R.id.fragmentContainer
}