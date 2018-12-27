package com.prembros.facilis.cardwithlist

import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.prembros.facilis.activity.BaseCardActivity
import com.prembros.facilis.sample.R
import com.prembros.facilis.util.*
import kotlinx.android.synthetic.main.item_card_list.view.*
import org.jetbrains.anko.toast
import kotlin.random.Random

class CardWithListAdapter(private val activity: BaseCardActivity) : RecyclerView.Adapter<CardWithListAdapter.CardWithListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardWithListViewHolder =
            CardWithListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_card_list, parent, false))

    override fun onBindViewHolder(holder: CardWithListViewHolder, position: Int) {
        holder.itemView.run {
            cardListItem.text = cardListItem.context.getString(R.string.card_item_, (position + 1))
            rootCard.setCardBackgroundColor(getRandomMaterialColor())
            rootCard.onReducingClick {
                rootCard.context.toast("This is reducing click")
                activity.pushFragment(CardWithListFragment.newInstance(false))
            }
        }
    }

    private fun getRandomBoolean(): Boolean = arrayListOf(true, false)[Random.nextInt(1)]

    override fun getItemCount(): Int = 10

    class CardWithListViewHolder(view: View) : RecyclerView.ViewHolder(view)
}