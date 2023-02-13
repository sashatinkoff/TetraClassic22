package com.isidroid.b21.ui.main

import androidx.annotation.ColorRes
import com.isidroid.b21.R

interface MainView {
    var topInset: Int
    val isNightMode: Boolean

    fun updateProgress(isProgress: Boolean)
    fun updateStatusBarColor(
        @ColorRes statusBarColorRes: Int = android.R.color.transparent,
        @ColorRes navigationBarColorRes: Int = R.color.md_theme_light_primary,
        isLightStatusBarIcons: Boolean = false
    )
}