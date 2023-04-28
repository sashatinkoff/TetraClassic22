package com.isidroid.chart.ext

import android.content.res.Resources
import android.graphics.Canvas
import android.text.StaticLayout
import android.util.DisplayMetrics
import android.util.TypedValue
import androidx.core.graphics.withTranslation

val Number.dp get() = toFloat() * (Resources.getSystem().displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
val Number.sp get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, toFloat(), Resources.getSystem().displayMetrics)

fun StaticLayout.draw(canvas: Canvas, x: Float, y: Float) {
    canvas.withTranslation(x, y) {
        draw(this)
    }
}