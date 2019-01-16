package com.prembros.facilis.util

import com.prembros.facilis.SampleApp.Companion.appContext
import com.prembros.facilis.data.Tile
import com.prembros.facilis.sample.R

internal val tileList = arrayListOf(
        Tile(appContext().getString(R.string.card_with_list), R.color.faded_red, imageList[0]),
        Tile(appContext().getString(R.string.list_of_cards), R.color.cornflower_blue, imageList[1]),
        Tile(appContext().getString(R.string.plain_card), android.R.color.white, imageList[2]),
        Tile(appContext().getString(R.string.blur_popup_zoom), R.color.crusoe, imageList[3]),
        Tile(appContext().getString(R.string.blur_popup_float_up), R.color.maire, imageList[4]),
        Tile(appContext().getString(R.string.blur_popup_long_press), R.color.husk, imageList[5])
)