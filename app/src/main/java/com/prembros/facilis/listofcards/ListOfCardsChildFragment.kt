package com.prembros.facilis.listofcards

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.RelativeLayout
import com.prembros.facilis.fragment.BaseCardListChildFragment
import com.prembros.facilis.sample.R
import com.prembros.facilis.util.*
import com.squareup.picasso.*
import com.squareup.picasso.Target
import kotlinx.android.synthetic.main.fragment_list_of_cards_child.*

class ListOfCardsChildFragment : BaseCardListChildFragment() {

    private lateinit var imageUrl: String

    companion object {
        private const val IMAGE = "image"
        fun newInstance(imageUrl: String) = ListOfCardsChildFragment().apply { arguments = Bundle().apply { putString(IMAGE, imageUrl) } }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.run { imageUrl = getString(IMAGE)!! }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_list_of_cards_child, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Picasso.get().load(imageUrl).into(getThumbnailHeightManipulatingTarget())
    }

    private fun getThumbnailHeightManipulatingTarget() = object : Target {
        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}

        override fun onBitmapFailed(e: Exception, errorDrawable: Drawable?) {
            Log.e("onBitmapFailed", e.message, e)
        }

        override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
            if (activity == null) return

            val width = (activity!!.windowManager.defaultDisplay.getScreenSize()[0] - activity!!.getDp(18f)).toInt()
            val params = thumbnail.layoutParams as RelativeLayout.LayoutParams
            params.height = width * bitmap.height / bitmap.width
            thumbnail.layoutParams = params
            thumbnail.setImageBitmap(bitmap)
        }
    }

    override fun getDragView(): View? = dragArea

    override fun getRootView(): ViewGroup? = rootCard

    override fun dragHandleId(): Int = R.id.drag_handle_image
}