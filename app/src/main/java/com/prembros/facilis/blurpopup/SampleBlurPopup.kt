package com.prembros.facilis.blurpopup

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.AnimRes
import com.prembros.facilis.dialog.BaseBlurPopup
import com.prembros.facilis.sample.R
import com.prembros.facilis.util.*
import com.squareup.picasso.Picasso
import io.alterac.blurkit.BlurLayout
import kotlinx.android.synthetic.main.popup_sample_blur.*

class SampleBlurPopup : BaseBlurPopup() {

    @AnimRes
    private var enterAnim: Int = R.anim.zoom_in

    @AnimRes
    private var exitAnim: Int = R.anim.zoom_out

    companion object {
        private const val ENTER_ANIM = "enterAnim"
        private const val EXIT_ANIM = "ExitAnim"
        fun newInstance(@AnimRes enterAnim: Int = R.anim.zoom_in, @AnimRes exitAnim: Int = R.anim.zoom_out) = SampleBlurPopup().apply {
            arguments = Bundle().apply {
                putInt(ENTER_ANIM, enterAnim)
                putInt(EXIT_ANIM, exitAnim)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.run {
            enterAnim = getInt(ENTER_ANIM, R.anim.zoom_in)
            exitAnim = getInt(EXIT_ANIM, R.anim.zoom_out)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.popup_sample_blur, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val url = getRandomImageUrl()
        Log.d("URL: ", url)
        Picasso.get().load(url).into(popupThumbnail.dynamicHeightTarget(activity))
    }

    /**
     * Optional - override for customizing enter animation of the root view returned by [getRootView()]
     */
    override fun enterAnimation(): Int = enterAnim

    /**
     * Optional - override for customizing exit animation of the root view returned by [getRootView()]
     */
    override fun dismissAnimation(): Int = exitAnim

    /**
     * Return the background layout (preferably BlurLayout) for effects during transition, return null for transparent background and no effects
     */
    override fun getBlurLayout(): BlurLayout? = blurLayout

    /**
     * Return the Drag area that should be used for swipe gestures, return null for no drag gesture implementations
     */
    override fun getDragHandle(): View? = dragArea

    /**
     * Return the root view (preferably a CardView) of the fragment
     */
    override fun getRootView(): View? = rootCard

    /**
     * Return the background layout (preferably BlurLayout) for effects during transition, return null for transparent background and no effects
     */
    override fun getBackgroundLayout(): ViewGroup? = blurLayout
}