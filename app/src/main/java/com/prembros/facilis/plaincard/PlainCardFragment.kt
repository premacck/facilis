package com.prembros.facilis.plaincard

import android.os.Bundle
import android.view.*
import com.prembros.facilis.fragment.BaseCardFragment
import com.prembros.facilis.sample.R
import com.prembros.facilis.util.getRandomMaterialColor
import kotlinx.android.synthetic.main.fragment_plain_card.*

class PlainCardFragment : BaseCardFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_plain_card, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootCard.setCardBackgroundColor(getRandomMaterialColor())
    }

    override fun getBackgroundBlurLayout(): ViewGroup? = blurLayout

    override fun getDragView(): View? = dragArea

    override fun getRootView(): ViewGroup? = rootCard

    override fun dragHandleId(): Int = R.id.drag_handle_image
}