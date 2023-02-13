package com.isidroid.core.ui

import androidx.annotation.NavigationRes
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import com.isidroid.core.R

interface NavigationListener {
    val navController: NavController

    val defaultOptions
        get() = NavOptions.Builder()
            .setEnterAnim(R.anim.nav_default_enter_anim)
            .setExitAnim(R.anim.nav_default_exit_anim)
            .setPopEnterAnim(R.anim.nav_default_pop_enter_anim)
            .setPopExitAnim(R.anim.nav_default_pop_exit_anim)

    fun navigateTo(action: NavDirections, optionsBuilder: ((NavOptions.Builder) -> Unit)? = null) {
        optionsBuilder?.invoke(defaultOptions)
        navController.navigate(action, navOptions = defaultOptions.build())
    }

    fun navigateTo(action: NavDeepLinkRequest, optionsBuilder: ((NavOptions.Builder) -> Unit)? = null) {
        optionsBuilder?.invoke(defaultOptions)
        navController.navigate(action, navOptions = defaultOptions.build())
    }

    fun setNavGraph(@NavigationRes graphResId: Int) = apply {
        if (navController.graph.id != graphResId)
            navController.setGraph(graphResId)
    }
}