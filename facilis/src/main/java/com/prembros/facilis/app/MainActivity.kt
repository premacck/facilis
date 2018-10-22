package com.prembros.facilis.app

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.prembros.facilis.R

class MainActivity : AppCompatActivity(), FragmentManager.OnBackStackChangedListener {

    companion object {
        var index: Int = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pushFragment(newSampleFragmentInstance(index), false)
    }

    fun pushFragment(fragment: Fragment, isAddToBackStack: Boolean) {
        if (index > 0) {
            val previousFragment: SampleFragment = supportFragmentManager.findFragmentByTag(SampleFragment::class.java.simpleName + index) as SampleFragment
            previousFragment.moveToBackGround()
        }
        index++

        if (index <= 0) return
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.float_up, R.anim.sink_up, R.anim.float_down, R.anim.sink_down)
                .add(R.id.fragmentContainer, fragment, SampleFragment::class.java.simpleName + index)
                .addToBackStack(isAddToBackStack)
                .commit()
        Log.i("pushFragment", "Added index: $index")
    }

    fun popBackStack() {
        if (index <= 0) return
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStackImmediate()
            Log.i("popBackStack", "popped index: ${index--}")
            if (supportFragmentManager.backStackEntryCount >= 0) {
                val previousFragment: SampleFragment = supportFragmentManager.findFragmentByTag(SampleFragment::class.java.simpleName + index) as SampleFragment
                previousFragment.moveToForeGround()
            }
        }
    }

    private fun FragmentTransaction.addToBackStack(addFlag: Boolean): FragmentTransaction {
        return if (addFlag) this.addToBackStack(SampleFragment::class.java.simpleName) else this
    }

    override fun onBackStackChanged() {
        index = supportFragmentManager.backStackEntryCount
//        rootFadedCardLayout.visibility = if (index > 0) VISIBLE else GONE
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}
