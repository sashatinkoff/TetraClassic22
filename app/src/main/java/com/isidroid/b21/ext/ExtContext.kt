package com.isidroid.b21.ext

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Parcelable
import android.util.DisplayMetrics
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import kotlin.math.roundToInt

fun screenWidthPx() = Resources.getSystem().displayMetrics.widthPixels
fun screenHeightPx() = Resources.getSystem().displayMetrics.heightPixels

fun Context.dpToPx(dp: Int) =
    with(resources.displayMetrics) { (dp * (xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt() }

fun Context.dpToPxf(dp: Int): Float {
    val metrics = Resources.getSystem().displayMetrics
    return dp * (metrics.densityDpi / 160f)
}

fun Context.color(@ColorRes color: Int) = ContextCompat.getColor(this, color)
fun Context.hasPermission(name: String) =
    ContextCompat.checkSelfPermission(this, name) == PackageManager.PERMISSION_GRANTED

fun Context.hasAllPermissions(vararg perms: String) = perms.all { hasPermission(it) }

fun Intent.putExtra(key: String, value: List<Parcelable>?) =
    apply {
        value ?: return@apply
        putExtra(key, value.toTypedArray())
    }