package com.isidroid.b21.ui

import androidx.core.view.ViewCompat
import androidx.navigation.NavController
import com.google.android.material.appbar.MaterialToolbar


/**
 * USAGE in Fragment / Activity with Toolbar
 * (fr as? AppBarListener ?: this).createToolbar(toolbar, navController)
 */
interface AppBarListener {
    fun createToolbar(toolbar: MaterialToolbar, navController: NavController) {
        ViewCompat.setElevation(toolbar, 0f)
        toolbar.menu.clear()
        toolbar.navigationIcon = null
    }
}
