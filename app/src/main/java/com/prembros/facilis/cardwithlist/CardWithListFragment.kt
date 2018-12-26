package com.prembros.facilis.cardwithlist

import android.os.Bundle
import android.view.*
import com.prembros.facilis.fragment.BaseCardFragment
import com.prembros.facilis.sample.R
import kotlinx.android.synthetic.main.fragment_card_with_list.*

class CardWithListFragment : BaseCardFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_card_with_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.adapter = CardWithListAdapter()
    }

    override fun getBackgroundBlurLayout(): ViewGroup? = blurLayout

    override fun getDragView(): View? = dragArea

    override fun getRootView(): ViewGroup? = rootCard

    override fun dragHandleId(): Int = R.id.drag_handle_image
}
