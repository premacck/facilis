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

    /**
     * Return the number of items to be inflated in the [viewPager]
     */
    override fun baseCardCount(): Int = 10

    /**
     * Return the background layout (preferably BlurLayout) for effects during transition, return null for transparent background and no effects
     */
    override fun backgroundBlurLayout(): ViewGroup? = blurLayout

    /**
     * Return the [ViewPager] which will contain the [BaseCardFragment]s
     */
    override fun viewPager(): ViewPager = cardViewPager

    /**
     * Put here what you'd put in the [getItem(position: Int)] method of the [ViewPager]
     */
    override fun baseCardToInflate(position: Int): BaseCardFragment = ListOfCardsChildFragment.newInstance(getRandomImageUrl())
}
