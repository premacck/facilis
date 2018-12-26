package com.prembros.facilis.fragment

import android.os.Bundle
import android.view.*
import androidx.annotation.CallSuper
import androidx.fragment.app.*
import androidx.viewpager.widget.ViewPager
import com.prembros.facilis.activity.BaseCardActivity
import com.prembros.facilis.util.*
import org.jetbrains.anko.support.v4.find
import java.lang.ref.WeakReference

abstract class BaseCardListContainerFragment : BaseFragment(), CardContainer {

    private lateinit var pagerAdapter: BaseCardContainerPagerAdapter
    private var isInForeGround: Boolean = false

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pagerAdapter = BaseCardContainerPagerAdapter(childFragmentManager, this)
        viewPager().adapter = pagerAdapter
        viewPager().currentItem = initialPosition()
        (activity as? BaseCardActivity)?.run { if (index > 0) viewPager().floatUp() }
        isInForeGround = true
    }

    open fun initialPosition(): Int = 0

    abstract fun baseCardCount(): Int

    abstract fun backgroundBlurLayout(): ViewGroup?

    abstract fun viewPager(): ViewPager

    override fun getRootView(): ViewGroup? = viewPager()

    abstract fun baseCardToInflate(position: Int): BaseCardFragment

    override fun moveToBackGround() {
        viewPager().moveToBackGround(parentActivity().index)
        pagerAdapter.currentCard?.run {
            find<View>(dragHandleId()).fadeOut()
        }
        isInForeGround = false
    }

    override fun moveToForeGround() {
        viewPager().moveToForeGround()
        pagerAdapter.currentCard?.run {
            find<View>(dragHandleId()).fadeIn()
        }
        isInForeGround = true
    }

    override fun parentActivity(): BaseCardActivity = (activity as? BaseCardActivity)
            ?: throw IllegalStateException("Fragment must be attached to a BaseCardActivity")

    class BaseCardContainerPagerAdapter(fm: FragmentManager, baseCardContainerFragment: BaseCardListContainerFragment) : FragmentStatePagerAdapter(fm) {

        private val ref: WeakReference<BaseCardListContainerFragment> = WeakReference(baseCardContainerFragment)
        var currentCard: BaseCardFragment? = null

        override fun getItem(position: Int): Fragment? = ref.get()?.baseCardToInflate(position)

        override fun getCount(): Int = ref.get()?.baseCardCount() ?: 0

        override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
            if (currentCard !== `object`) {
                currentCard = `object` as? BaseCardFragment
            }
            super.setPrimaryItem(container, position, `object`)
        }
    }
}