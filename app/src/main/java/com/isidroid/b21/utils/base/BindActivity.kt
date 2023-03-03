package com.isidroid.b21.utils.base

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updateLayoutParams
import com.google.android.material.color.MaterialColors
import com.isidroid.b21.R
import com.isidroid.b21.data.source.settings.Settings
import com.isidroid.b21.ui.main.MainView
import com.isidroid.core.ext.color
import com.isidroid.core.ui.ErrorUiHandler
import com.isidroid.core.ui.ScreenUiListener
import com.isidroid.core.ui.StatusColorListener
import com.isidroid.core.ui.core.CoreBindActivity
import com.isidroid.core.utils.WindowInsetListener

abstract class BindActivity : CoreBindActivity(), ErrorUiHandler, ScreenUiListener, StatusColorListener {
    // ScreenUiListener
    override var topInset: Int = 0
    override val isNightMode: Boolean
        get() = resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

    // StatusColorListener
    override val statusBarColorRes: Int get() = com.isidroid.core.R.attr.color_transparent
    override val navigationBarColorRes: Int get() = com.google.android.material.R.attr.colorSurface
    override val isLightStatusBarIcons: Boolean get() = !isNightMode

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(Settings.theme)
        super.onCreate(savedInstanceState)
    }

    override fun showError(t: Throwable?) {
        t ?: return
        Toast.makeText(this, t.message, Toast.LENGTH_LONG).show()
    }

    // ScreenUiListener
    override fun updateStatusBarColor(@AttrRes statusBarColorRes: Int, @AttrRes navigationBarColorRes: Int, isLightStatusBarIcons: Boolean) {
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = isLightStatusBarIcons
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightNavigationBars = isLightStatusBarIcons

        window.statusBarColor = MaterialColors.getColor(this, statusBarColorRes, color(android.R.color.transparent))
        window.navigationBarColor = MaterialColors.getColor(this, navigationBarColorRes, color(android.R.color.transparent))
    }

}