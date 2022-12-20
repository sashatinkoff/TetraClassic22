package com.isidroid.b21.ui

import androidx.annotation.NavigationRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import com.isidroid.b21.R

interface NavigationListener {
    val navController: NavController

    fun navigateTo(action: NavDirections, optionsBuilder: ((NavOptions.Builder) -> Unit)? = null)
    fun setNavGraph(@NavigationRes graphResId: Int = R.navigation.nav_main) = apply {
        if (navController.graph.id != graphResId)
            navController.setGraph(graphResId)
    }
}