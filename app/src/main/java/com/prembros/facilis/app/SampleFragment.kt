package com.prembros.facilis.app

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.prembros.facilis.app.MainActivity.Companion.index
import com.prembros.facilis.library.fragmentstack.card.BaseCard
import com.prembros.facilis.library.util.getDp
import com.prembros.facilis.library.util.moveToBackGround
import com.prembros.facilis.library.util.moveToForeGround
import com.prembros.facilis.library.util.setSwipeDownListener
import com.prembros.facilis.sample.R
import kotlinx.android.synthetic.main.fragment_sample.*

private const val ARG_INDEX = "index"
fun newSampleFragmentInstance(index: Int) = SampleFragment().apply { arguments = Bundle().apply { putInt(ARG_INDEX, index) } }

class SampleFragment : BaseCard() {

    private var fragmentIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.run {
            fragmentIndex = getInt(ARG_INDEX)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sample, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        indexTextView.text = getString(R.string.card_index, fragmentIndex)

        setup(activity, index)

        addFragmentButton.setOnClickListener {
            getParentActivity().pushFragment(
                    newSampleFragmentInstance(index),
                    true
            )
        }
        removeFragmentButton.setOnClickListener { getParentActivity().popBackStack() }
    }

    override fun adjustWithIndex(index: Int) {
        rootFadedCardLayout?.visibility = if (index > 1) View.VISIBLE else View.GONE

        rootCard?.run {
            val params = layoutParams as RelativeLayout.LayoutParams
            params.topMargin = getDp(context!!, if (index > 1) 20f else 0f).toInt()
            layoutParams = params
        }
    }

    override fun setupSwipeDownGesture(activity: Activity) {
        dragHandle?.setSwipeDownListener(activity, dragHandle, rootCard, rootFadedCardLayout)
    }

    override fun dispose() {
        dragHandle.setOnTouchListener(null)
    }

    override fun moveToBackGround() {
        rootCard.moveToBackGround()
    }

    override fun moveToForeGround() {
        rootCard.moveToForeGround()
    }

    override fun getParentActivity(): MainActivity {
        if (activity is MainActivity)
            return activity as MainActivity
        else throw IllegalStateException("Fragment must be attached to MainActivity")
    }
}
