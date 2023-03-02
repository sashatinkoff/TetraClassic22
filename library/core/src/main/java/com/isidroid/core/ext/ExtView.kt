package com.isidroid.core.ext

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Build.VERSION
import android.text.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.viewbinding.ViewBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
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


fun randomColor(): Int = Color.argb(255, Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))

inline fun NestedScrollView.activateOnScrollToFooter(crossinline onScrolled: (Boolean) -> Unit) {
    if (VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
    } else {
        onScrolled(true)
    }
}

fun View.animateAlpha(alpha: Float) {
    this.alpha = 1f
    visible(true)

    animate()
        .alpha(alpha)
        .setDuration(400)
        .withEndAction { if (alpha == 0f) visible(false) }
        .start()
}

@SuppressLint("ClickableViewAccessibility")
inline fun TextView.setOnDrawableClickListeners(
    crossinline onStartClicked: () -> Unit = {},
    crossinline onEndClicked: () -> Unit = {}
) {
    setOnTouchListener { _, event ->
        if (event.action == MotionEvent.ACTION_UP) {
            val textLocation = IntArray(2)
            getLocationOnScreen(textLocation)

            when {
                event.rawX <= textLocation[0] + totalPaddingLeft -> onStartClicked()
                event.rawX >= textLocation[0] + width - totalPaddingRight -> onEndClicked()
            }
        }
        true
    }
}

fun View.obtainStyledAttributes(attrsSet: AttributeSet?, attrsId: IntArray): TypedArray {
    return context.theme.obtainStyledAttributes(attrsSet, attrsId, 0, 0)
}

fun roundedRect(
    left: Int,
    top: Int,
    right: Int,
    bottom: Int,
    topLeftCorner: Float,
    topRightCorner: Float,
    bottomRightCorner: Float,
    bottomLeftCorner: Float,
    color: Int
): Drawable {
    val backgroundShapeModel = ShapeAppearanceModel.builder()
        .setTopLeftCorner(CornerFamily.ROUNDED, topLeftCorner)
        .setTopRightCorner(CornerFamily.ROUNDED, topRightCorner)
        .setBottomRightCorner(CornerFamily.ROUNDED, bottomRightCorner)
        .setBottomLeftCorner(CornerFamily.ROUNDED, bottomLeftCorner)
        .build()
    return MaterialShapeDrawable(backgroundShapeModel).apply {
        fillColor = ColorStateList.valueOf(color)
        setBounds(left, top, right, bottom)
    }
}