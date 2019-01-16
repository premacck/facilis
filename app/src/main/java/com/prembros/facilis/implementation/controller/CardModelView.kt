package com.prembros.facilis.implementation.controller

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.airbnb.epoxy.*
import com.prembros.facilis.activity.BaseCardActivity
import com.prembros.facilis.blurpopup.SampleBlurPopup
import com.prembros.facilis.cardwithlist.CardWithListFragment
import com.prembros.facilis.data.Tile
import com.prembros.facilis.dialog.*
import com.prembros.facilis.listofcards.ListOfCardsContainerFragment
import com.prembros.facilis.longpress.*
import com.prembros.facilis.plaincard.PlainCardFragment
import com.prembros.facilis.sample.R
import com.prembros.facilis.util.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_card_tile.view.*
import org.jetbrains.anko.backgroundColor

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class CardModelView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes), DebouncingClickListener, PopupInflaterListener, PopupStateListener, PopupHoverListener {

    private var activity: BaseCardActivity? = null
    private var longPressBlurPopup: LongPressBlurPopup? = null

    init {
        View.inflate(context, R.layout.item_card_tile, this)
    }

    @Suppress("DEPRECATION")
    @ModelProp
    fun withTile(pair: Pair<Tile, BaseCardActivity>) {
        val tile = pair.first
        activity = pair.second
        longPressBlurPopup.checkAndUnregister()

        cardTextView.text = tile.text
        cardRoot.backgroundColor = context.resources.getColor(tile.colorRes)
        cardRoot.onDebouncingClick(this)
        Picasso.get().load(tile.imageUrl).into(cardThumb)

        longPressBlurPopup = LongPressBlurPopup.Builder
                .with(activity!!)
                .targetView(cardRoot)
                .baseBlurPopup(SampleBlurPopup.newInstance())
                .animationType(AnimType.ANIM_FROM_BOTTOM)
                .popupStateListener(this)
                .hoverListener(this)
                .build()

        longPressBlurPopup?.register()
    }

    override fun onDebouncingClick(view: View) {
        when (cardTextView.text) {
            context.getString(R.string.card_with_list) -> activity?.pushFragment(CardWithListFragment.newInstance(true))
            context.getString(R.string.list_of_cards) -> activity?.pushFragment(ListOfCardsContainerFragment())
            context.getString(R.string.plain_card) -> activity?.pushFragment(PlainCardFragment())
            context.getString(R.string.blur_popup_zoom) -> activity?.pushPopup(SampleBlurPopup.newInstance(R.anim.zoom_in, R.anim.zoom_out))
            context.getString(R.string.blur_popup_float_up) -> activity?.pushPopup(SampleBlurPopup.newInstance(R.anim.float_up, R.anim.sink_down))
        }
    }

    override fun onViewInflated(tag: String?, rootView: View?) {
    }

    override fun onPopupShow(popupTag: String?) {
    }

    override fun onPopupDismiss(popupTag: String?) {
    }

    override fun onHoverChanged(view: View, isHovered: Boolean) {
    }
}