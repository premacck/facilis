package com.prembros.facilis.cardwithlist

import android.os.Bundle
import android.view.*
import com.prembros.facilis.fragment.BaseCardFragment
import com.prembros.facilis.sample.R
import com.prembros.facilis.util.getRandomMaterialColor
import kotlinx.android.synthetic.main.fragment_card_with_list.*
import org.jetbrains.anko.backgroundColor

class CardWithListFragment : BaseCardFragment() {

    companion object {
        private const val IS_BLUR_ENABLED = "isBlurEnabled"
        fun newInstance(isBlurEnabled: Boolean) = CardWithListFragment().apply { arguments = Bundle().apply { putBoolean(IS_BLUR_ENABLED, isBlurEnabled) } }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.run { isBlurEnabled = getBoolean(IS_BLUR_ENABLED, true) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_card_with_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        header.text = getString(R.string.this_is_a_card_fragment_with_a_recyclerview, parentActivity().index)
        header.backgroundColor = getRandomMaterialColor()
        recyclerView.adapter = CardWithListAdapter(parentActivity())
    }

    override fun getBackgroundBlurLayout(): ViewGroup? = blurLayout

    override fun getDragView(): View? = dragArea

    override fun getRootView(): ViewGroup? = rootCard

    override fun dragHandleId(): Int = R.id.drag_handle_image
}
