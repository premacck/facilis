package com.prembros.facilis.data

import android.os.Parcelable
import androidx.annotation.ColorRes
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Tile(var text: String, @ColorRes var colorRes: Int, var imageUrl: String) : Parcelable