package com.prembros.facilis.app

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.graphics.ColorUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout

import com.prembros.facilis.R
import com.prembros.facilis.app.MainActivity.Companion.index
import com.prembros.facilis.library.OnSwipeTouchListener
import com.prembros.facilis.library.util.getDp
import com.prembros.facilis.library.util.toggleViewinteraction
import kotlinx.android.synthetic.main.fragment_sample.*

private const val ARG_INDEX = "index"
fun newSampleFragmentInstance(index: Int) =
        SampleFragment().apply { arguments = Bundle().apply { putInt(ARG_INDEX, index) } }

@Suppress("DEPRECATION")
class SampleFragment : Fragment() {

    private lateinit var rootView: View
    private var fragmentIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { it ->
            fragmentIndex = it.getInt(ARG_INDEX)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_sample, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        indexTextView.text = getString(R.string.card_index, fragmentIndex)

        val layoutParams = rootCard.layoutParams as FrameLayout.LayoutParams
        layoutParams.topMargin = getDp(context!!, if (index > 1) 20f else 0f).toInt()
        rootCard.layoutParams = layoutParams
        addFragmentButton.setOnClickListener {
            getParentActivity().pushFragment(
                    newSampleFragmentInstance(index),
                    true
            )
        }
        removeFragmentButton.setOnClickListener { getParentActivity().popBackStack() }

        dragHandle.setOnTouchListener(object : OnSwipeTouchListener(getParentActivity(), dragHandle, rootCard, null) {
            override fun onSwipeDown() {
                getParentActivity().onBackPressed()
            }
        })
    }

    fun moveToBackGround() {
        val objectAnimator = ObjectAnimator.ofPropertyValuesHolder(
                rootCard,
                PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, if (index > 1) -getDp(context!!, 34f) else -getDp(context!!, 15f)),
                PropertyValuesHolder.ofFloat(View.SCALE_X, 0.95f),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.95f)
        ).prepare()
        objectAnimator.addUpdateListener {
            rootCard.setCardBackgroundColor(
                    ColorUtils.blendARGB(
                            Color.WHITE,
                            resources.getColor(R.color.others_color_grey),
                            it.animatedFraction
                    )
            )
        }
        objectAnimator.start()
        rootCard.elevation = index.toFloat()
        rootCard.toggleViewinteraction(false)
        dragHandle.toggleViewinteraction(false)
    }

    fun moveToForeGround() {
        val objectAnimator = ObjectAnimator.ofPropertyValuesHolder(
                rootCard,
                PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0f),
                PropertyValuesHolder.ofFloat(View.SCALE_X, 1f),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f)
        ).prepare()
        objectAnimator.addUpdateListener {
            rootCard.setCardBackgroundColor(
                    ColorUtils.blendARGB(
                            resources.getColor(R.color.others_color_grey),
                            Color.WHITE,
                            it.animatedFraction
                    )
            )
        }
        objectAnimator.start()
        rootCard.elevation = getDp(context!!, 8f)
        rootCard.toggleViewinteraction(true)
        dragHandle.toggleViewinteraction(true)
    }

    private fun ObjectAnimator.prepare(): ObjectAnimator {
        this.duration = 200
        this.interpolator = DecelerateInterpolator()
        return this
    }

    private fun getParentActivity(): MainActivity {
        if (activity is MainActivity)
            return activity as MainActivity
        else throw IllegalStateException("Fragment must be attached to MainActivity")
    }
}
