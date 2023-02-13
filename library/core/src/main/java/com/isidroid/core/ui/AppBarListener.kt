package com.isidroid.core.ui

import androidx.core.view.ViewCompat
import androidx.navigation.NavController
import com.google.android.material.appbar.MaterialToolbar
import com.isidroid.core.ext.visible


/**
 * USAGE in Fragment / Activity with Toolbar
 * (fr as? AppBarListener ?: this).createToolbar(toolbar, navController)
 */
interface AppBarListener {
    fun createToolbar(toolbar: MaterialToolbar, navController: NavController) {
        ViewCompat.setElevation(toolbar, 0f)
        toolbar.visible(false)
        toolbar.menu.clear()
        toolbar.navigationIcon = null
        toolbar.post { toolbar.subtitle = null }
        toolbar.setNavigationOnClickListener { navController.popBackStack() }
    }
}
