package com.prembros.facilis.library.card

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.prembros.facilis.R
import com.prembros.facilis.library.util.getDp
import com.prembros.facilis.library.util.moveToBackGround
import com.prembros.facilis.library.util.moveToForeGround
import com.prembros.facilis.library.util.setSwipeDownListener

class FacilisCard @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var rootFadedCardLayout: FrameLayout
    private var fadedCard: CardView
    private var rootCard: CardView
    private var rootCardChildContainer: RelativeLayout
    private var dragHandle: TextView

    init {
        val view = inflate(context, R.layout.facilis_card, this)
        rootFadedCardLayout = view.findViewById<FrameLayout>(R.id.rootFadedCardLayout)
        fadedCard = view.findViewById(R.id.fadedCard)
        rootCard = view.findViewById(R.id.rootCard)
        rootCardChildContainer = view.findViewById(R.id.rootCardChildContainer)
        dragHandle = view.findViewById(R.id.dragHandle)
    }

    fun setup(activity: Activity?, index: Int = 0) {
        adjustWithIndex(index)
        activity?.let { setupSwipeDownGesture(it) }
    }

    private fun adjustWithIndex(index: Int) {
        rootFadedCardLayout?.visibility = if (index > 1) View.VISIBLE else View.GONE

        rootCard?.run {
            val params = layoutParams as RelativeLayout.LayoutParams
            params.topMargin = getDp(context!!, if (index > 1) 20f else 0f).toInt()
            layoutParams = params
        }
    }

    private fun setupSwipeDownGesture(activity: Activity) {
        dragHandle?.setSwipeDownListener(activity, dragHandle, rootCard, rootFadedCardLayout)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun dispose() {
        dragHandle.setOnTouchListener(null)
    }

    fun moveToBackGround() {
        rootCard.moveToBackGround()
    }

    fun moveToForeGround() {
        rootCard.moveToForeGround()
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams?) {
//        super.addView(this, index, params)
        if (child.id != R.id.rootLayout && child.id != R.id.rootFadedCardLayout && child.id != R.id.fadedCard && child.id != R.id.rootCard && child.id != R.id.rootCardChildContainer && child.id != R.id.dragHandle) {
            try {
                rootCardChildContainer.addView(child, index, params)
                val lParams = child.layoutParams as RelativeLayout.LayoutParams
                lParams.addRule(RelativeLayout.BELOW, R.id.dragHandle)
                child.layoutParams = lParams
            } catch (e: Exception) {
                Log.e("AddView(): ", "index: $index: ", e)
            }
        }
    }
}
