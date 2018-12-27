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

    /**
     * Return the background layout (preferably BlurLayout) for effects during transition, return null for transparent background and no effects
     */
    override fun getBackgroundBlurLayout(): ViewGroup? = blurLayout

    /**
     * Return the Drag area that should be used for swipe gestures, return null for no drag gesture implementations
     */
    override fun getDragView(): View? = dragArea

    /**
     * Return the root view (preferably a CardView) of the fragment
     */
    override fun getRootView(): ViewGroup? = rootCard

    /**
     * Return the Drag handle resource ID to toggle visibility during transition
     */
    override fun dragHandleId(): Int = R.id.drag_handle_image
}