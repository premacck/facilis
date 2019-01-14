package com.prembros.facilis.longpress

interface PopupStateListener {

    fun onPopupShow(popupTag: String?)

    fun onPopupDismiss(popupTag: String?)
}