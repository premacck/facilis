package com.prembros.facilis.swiper

import android.app.Activity
import android.content.Context
import android.content.res.TypedArray
import android.util.*
import androidx.viewpager.widget.ViewPager
import com.prembros.facilis.R

private const val DEFAULT_PERCENTAGE_PADDING = 8
private const val MIN_ALPHA = .5f

class DeckViewPager : ViewPager {

    private val pageTransformer = CoverFlowTransformer(MIN_ALPHA)

    constructor(context: Context) : super(context) {
        setPercentagePadding(context, DEFAULT_PERCENTAGE_PADDING)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        val typedArray: TypedArray = context.obtainStyledAttributes(attributeSet, R.styleable.DeckViewPager)
        val percentagePaddingXml = typedArray.getInt(R.styleable.DeckViewPager_padding_percentage, Integer.MAX_VALUE)
        if (percentagePaddingXml != Integer.MAX_VALUE) {
            setPercentagePadding(context, percentagePaddingXml)
        }
        val dipPaddingXmlInPixel = typedArray.getDimensionPixelSize(R.styleable.DeckViewPager_padding_dp, Integer.MAX_VALUE)
        if (dipPaddingXmlInPixel != Integer.MAX_VALUE) {
            initProperties(context, dipPaddingXmlInPixel.toFloat())
        }
        val minAlphaXml = typedArray.getFloat(R.styleable.DeckViewPager_min_alpha, MIN_ALPHA)
        if (minAlphaXml != MIN_ALPHA) {
            setMinAlpha(minAlphaXml)
        }
        typedArray.recycle()

        // set the default padding if no properties from XML
        if (percentagePaddingXml == Integer.MAX_VALUE && dipPaddingXmlInPixel == Integer.MAX_VALUE) {
            setPercentagePadding(context, DEFAULT_PERCENTAGE_PADDING)
        }
    }

    init {
        initView()
    }

    private fun initView() {
        setPageTransformer(true, pageTransformer)
    }

    /**
     * Set left and right padding based on percentage of the screen width
     * If the percentage is more than 18, the left and right items height might not be consistent
     */
    fun setPercentagePadding(context: Context, percentage: Int) {
        when {
            percentage == 0 -> initProperties(context, 0f)
            percentage < 0 -> throw IllegalArgumentException("Percentage can't be lower than 0")
            percentage >= 50 -> throw IllegalArgumentException("Your layout will not visible if the percentage equals or higher than 50")
            else -> {
                val padding = screenWidth(context) * percentage / 100f
                initProperties(context, padding)
            }
        }
    }

    /**
     * Set left and right padding based on dp value
     */
    fun setDpPadding(context: Context, dp: Float) {
        val padding = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.resources.displayMetrics
        )
        initProperties(context, padding)
    }

    /**
     * Set minimum alpha for left and right views
     */
    fun setMinAlpha(minAlpha: Float) {
        when {
            minAlpha < 0 || minAlpha > 1 -> throw IllegalArgumentException("Minimum alpha must be between 0 and 1")
            else -> pageTransformer.minAlpha = minAlpha
        }
    }

    private fun initProperties(context: Context, padding: Float) {
        val intPadding = padding.toInt()
        setPadding(intPadding, 0, intPadding, 0)
        clipToPadding = false
        pageMargin = 0

        pageTransformer.paddingFactor = padding / screenWidth(context)
    }

    private fun screenWidth(context: Context): Int {
        val metrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(metrics)
        return metrics.widthPixels
    }
}