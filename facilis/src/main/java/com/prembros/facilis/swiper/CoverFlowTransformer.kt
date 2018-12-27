package com.prembros.facilis.swiper

import android.view.View
import androidx.viewpager.widget.ViewPager

private const val SCALE_MIN = .3f
private const val SCALE_MAX = 1f
private const val SCALE = .05f

class CoverFlowTransformer(var minAlpha: Float) : ViewPager.PageTransformer {

    var paddingFactor: Float = 0.08f

    override fun transformPage(page: View, position: Float) {
        val realPosition = position - paddingFactor
        val realScale = getFloat(1 - Math.abs(realPosition * SCALE), SCALE_MIN, SCALE_MAX)
        page.scaleX = realScale
        page.scaleY = realScale
        if (realPosition != 0f) {
            val scaleFactor = Math.max(SCALE_MIN, 1 - Math.abs(realPosition))
            page.alpha = minAlpha + (scaleFactor - SCALE_MIN) / (1 - SCALE_MIN) * (1 - minAlpha)
        }
    }

    private fun getFloat(value: Float, minValue: Float, maxValue: Float): Float {
        return Math.min(maxValue, Math.max(minValue, value))
    }
}