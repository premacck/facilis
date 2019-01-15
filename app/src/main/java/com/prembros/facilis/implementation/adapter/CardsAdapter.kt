package com.prembros.facilis.implementation.adapter

import android.view.*
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.prembros.facilis.data.Tile
import com.prembros.facilis.sample.R
import kotlinx.android.synthetic.main.item_card_tile.view.*
import org.jetbrains.anko.backgroundColor

class CardsAdapter(private val tiles: List<Tile>) : RecyclerView.Adapter<CardsAdapter.CardViewHolder>() {

    companion object {
        private const val TILE_CARD = 0
        private const val TILE_POPUP = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder = CardViewHolder(parent.inflateLayout(R.layout.item_card_tile))

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) = holder.bind(tiles[position])

    override fun getItemViewType(position: Int): Int = tiles[position].type

    override fun getItemCount(): Int = tiles.size

    @Suppress("DEPRECATION")
    class CardViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(tile: Tile) {
            itemView.run {
                cardTextView.text = tile.text
                cardRoot.backgroundColor = context.resources.getColor(tile.colorRes)
            }
        }
    }
}

fun ViewGroup.inflateLayout(@LayoutRes layoutRes: Int): View = LayoutInflater.from(context).inflate(layoutRes, this, false)