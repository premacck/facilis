package com.prembros.facilis.listofcards

import android.os.Bundle
import android.view.*
import com.prembros.facilis.fragment.BaseCardListChildFragment
import com.prembros.facilis.sample.R
import kotlinx.android.synthetic.main.fragment_list_of_cards_child.*

class ListOfCardsChildFragment : BaseCardListChildFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_list_of_cards_child, container, false)

    override fun getDragView(): View? = dragArea

    override fun getRootView(): ViewGroup? = rootCard

    override fun dragHandleId(): Int = R.id.drag_handle_image
}