package com.prembros.facilis.listofcards

import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.prembros.facilis.fragment.*
import com.prembros.facilis.util.getRandomImageUrl
import kotlinx.android.synthetic.main.fragment_list_of_cards_container.*

class ListOfCardsContainerFragment : BaseCardListContainerFragment() {

    override fun baseCardCount(): Int = 10

    override fun backgroundBlurLayout(): ViewGroup? = blurLayout

    override fun viewPager(): ViewPager = cardViewPager

    override fun baseCardToInflate(position: Int): BaseCardFragment = getRandomImageUrl()
}