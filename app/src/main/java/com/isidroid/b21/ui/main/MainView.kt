package com.isidroid.b21.ui.main

import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavDirections
import com.isidroid.b21.R
import com.isidroid.b21.data.source.settings.Settings

interface MainView {
    val popupToDestinationId: Int?

    fun updateStatusBarColor(@ColorRes colorRes: Int = R.color.md_theme_light_background, isLightStatusBarIcons: Boolean = true)
    fun navigateTo(action: NavDirections, clearBackStack: Boolean = false)
}