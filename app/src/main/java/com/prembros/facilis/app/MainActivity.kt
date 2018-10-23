package com.prembros.facilis.app

import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import com.prembros.facilis.library.fragmentstack.card.BaseCard
import com.prembros.facilis.library.util.movePreviousCardToBackground
import com.prembros.facilis.library.util.movePreviousCardToForeground
import com.prembros.facilis.library.util.pushCard
import com.prembros.facilis.sample.R

class MainActivity : AppCompatActivity(), FragmentManager.OnBackStackChangedListener {

    companion object {
        var index: Int = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pushFragment(newSampleFragmentInstance(index), false)
    }

    fun pushFragment(fragment: BaseCard, isAddToBackStack: Boolean) {
        if (index < 0) return

        if (index > 0) supportFragmentManager.movePreviousCardToBackground()

        supportFragmentManager.pushCard(R.id.fragmentContainer, fragment, fragment.javaClass.simpleName + index, index, isAddToBackStack)
        index++
    }

    fun popBackStack() {
        if (index <= 0) return
        if (supportFragmentManager.backStackEntryCount > 0) {
            index--
            supportFragmentManager.popBackStackImmediate()
            supportFragmentManager.movePreviousCardToForeground()
        }
    }

    override fun onBackStackChanged() {
        index = supportFragmentManager.backStackEntryCount
    }

    override fun onDestroy() {
        index = 0
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}
