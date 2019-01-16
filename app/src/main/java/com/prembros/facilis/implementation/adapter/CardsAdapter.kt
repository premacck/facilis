package com.prembros.facilis.implementation.adapter

import android.view.*
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.prembros.facilis.activity.BaseCardActivity
import com.prembros.facilis.blurpopup.SampleBlurPopup
import com.prembros.facilis.cardwithlist.CardWithListFragment
import com.prembros.facilis.data.Tile
import com.prembros.facilis.dialog.AnimType.Companion.ANIM_FROM_BOTTOM
import com.prembros.facilis.dialog.LongPressBlurPopup
import com.prembros.facilis.listofcards.ListOfCardsContainerFragment
import com.prembros.facilis.longpress.*
import com.prembros.facilis.plaincard.PlainCardFragment
import com.prembros.facilis.sample.R
import com.prembros.facilis.util.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_card_tile.view.*
import org.jetbrains.anko.*

class CardsAdapter(private val activity: BaseCardActivity) : RecyclerView.Adapter<CardsAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder = CardViewHolder(parent.inflateLayout(R.layout.item_card_tile), activity)

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) = holder.bind(tileList[position])

    override fun getItemCount(): Int = tileList.size

    @Suppress("DEPRECATION")
    class CardViewHolder(view: View, private val activity: BaseCardActivity) : RecyclerView.ViewHolder(view), DebouncingClickListener, PopupInflaterListener , PopupStateListener, PopupHoverListener {

        private var longPressBlurPopup: LongPressBlurPopup? = null

        fun bind(tile: Tile) {
            longPressBlurPopup.checkAndUnregister()
            itemView.run {
                cardTextView.text = tile.text
                cardRoot.backgroundColor = context.resources.getColor(tile.colorRes)
                cardRoot.onDebouncingClick(this@CardViewHolder)
                Picasso.get().load(tile.imageUrl).into(cardThumb)

                longPressBlurPopup = LongPressBlurPopup.Builder
                        .with(activity)
                        .targetView(cardRoot)
                        .baseBlurPopup(SampleBlurPopup.newInstance())
                        .animationType(ANIM_FROM_BOTTOM)
                        .popupStateListener(this@CardViewHolder)
                        .hoverListener(this@CardViewHolder)
                        .build()
            }
            longPressBlurPopup?.register()
        }

        override fun onDebouncingClick(view: View) {
            when (itemView.cardTextView.text) {
                view.context.getString(R.string.card_with_list) -> activity.pushFragment(CardWithListFragment.newInstance(true))
                view.context.getString(R.string.list_of_cards) -> activity.pushFragment(ListOfCardsContainerFragment())
                view.context.getString(R.string.plain_card) -> activity.pushFragment(PlainCardFragment())
                view.context.getString(R.string.blur_popup_zoom) -> activity.pushPopup(SampleBlurPopup.newInstance(R.anim.zoom_in, R.anim.zoom_out))
                view.context.getString(R.string.blur_popup_float_up) -> activity.pushPopup(SampleBlurPopup.newInstance(R.anim.float_up, R.anim.sink_down))
                view.context.getString(R.string.blur_popup_long_press) -> activity.toast("long press for popup")
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
}

fun ViewGroup.inflateLayout(@LayoutRes layoutRes: Int): View = LayoutInflater.from(context).inflate(layoutRes, this, false)