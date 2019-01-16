package com.prembros.facilis.implementation.controller

import com.airbnb.epoxy.TypedEpoxyController
import com.prembros.facilis.activity.BaseCardActivity
import com.prembros.facilis.data.Tile

class CardsController(private val activity: BaseCardActivity) : TypedEpoxyController<List<Tile>>() {

    override fun buildModels(tiles: List<Tile>?) {
        tiles?.forEach { tile ->
            CardModelViewModel_()
                    .id(tiles.indexOf(tile))
                    .withTile(Pair(tile, activity))
                    .addTo(this)
        }
    }
}
