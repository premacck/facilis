package com.prembros.facilis.listofcards

import android.os.Bundle
import android.view.*
import androidx.viewpager.widget.ViewPager
import com.prembros.facilis.fragment.*
import com.prembros.facilis.sample.R
import com.prembros.facilis.util.getRandomImageUrl
import kotlinx.android.synthetic.main.fragment_list_of_cards_container.*

class ListOfCardsContainerFragment : BaseCardListContainerFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_list_of_cards_container, container, false)

    override fun baseCardCount(): Int = 10

    override fun backgroundBlurLayout(): ViewGroup? = blurLayout

    override fun viewPager(): ViewPager = cardViewPager

    override fun baseCardToInflate(position: Int): BaseCardFragment = ListOfCardsChildFragment.newInstance(getRandomImageUrl())
}
