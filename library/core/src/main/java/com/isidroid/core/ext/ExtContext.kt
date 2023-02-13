package com.isidroid.core.ext

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.util.DisplayMetrics
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import java.io.File
import java.util.*
import kotlin.math.roundToInt


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

fun Context.createTempFile(
    prefix: String = UUID.randomUUID().toString(),
    suffix: String,
    deleteOnExit: Boolean = false
): File = File.createTempFile(prefix, ".$suffix", cacheDir).apply {
    createNewFile()

    if (deleteOnExit)
        deleteOnExit()
}