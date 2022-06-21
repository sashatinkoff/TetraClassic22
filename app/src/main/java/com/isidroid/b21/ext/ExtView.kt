package com.isidroid.b21.ext

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.core.widget.NestedScrollView
import androidx.viewbinding.ViewBinding
import com.google.android.material.button.MaterialButton
import kotlin.random.Random


fun View.enable(enabled: Boolean, alpha: Float = .6f) = apply {
    val inActiveAlpha = when {
        this is MaterialButton && alpha == .6f -> .4f
        else -> alpha
    }

    this.alpha = if (enabled) 1f else inActiveAlpha
    isEnabled = enabled
}

fun View.visible(isVisible: Boolean, isInvisible: Boolean = false) {
    visibility = when {
        isVisible -> View.VISIBLE
        isInvisible -> View.INVISIBLE
        else -> View.GONE
    }
}


val ViewBinding.context: Context
    get() = root.context


@JvmName("updateLayoutParamsTyped")
inline fun <reified T : ViewGroup.LayoutParams> View.updateSize(
    size: Int,
    noinline block: (T.() -> Unit)? = null
) {
    val params = layoutParams as T
    params.width = size
    params.height = size
    layoutParams = params

    block?.also { doOnLayout { block(params) } }
}

fun randomColor(): Int = Color.argb(255, Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))


fun NestedScrollView.activateOnScrollToFooter(onScrolled: (Boolean) -> Unit) {
    var isFooter = false

    setOnScrollChangeListener { _, _, scrollY, _, _ ->
        val verticalScrollableHeight = getChildAt(0).measuredHeight - measuredHeight
        val verticalPercentage = scrollY.toFloat() / verticalScrollableHeight
        val y = .9f

        if (verticalPercentage >= y && !isFooter) {
            isFooter = true
            onScrolled(true)
        } else if (verticalPercentage < y && isFooter) {
            isFooter = false
            onScrolled(false)
        }
    }
}