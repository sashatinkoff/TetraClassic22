package com.isidroid.core.ui

import android.os.Bundle
import androidx.annotation.NavigationRes
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import com.isidroid.core.R

interface NavigationListener {
    val navController: NavController

    fun defaultOptions() = NavOptions.Builder()
        .setEnterAnim(R.anim.nav_default_enter_anim)
        .setExitAnim(R.anim.nav_default_exit_anim)
        .setPopEnterAnim(R.anim.nav_default_pop_enter_anim)
        .setPopExitAnim(R.anim.nav_default_pop_exit_anim)

    fun navigateTo(action: NavDirections, optionsBuilder: ((NavOptions.Builder) -> Unit)? = null) {
        val options = defaultOptions()
        optionsBuilder?.invoke(options)

        navController.navigate(action, navOptions = options.build())
    }

    fun navigateTo(action: NavDeepLinkRequest, optionsBuilder: ((NavOptions.Builder) -> Unit)? = null) {
        val options = defaultOptions()
        optionsBuilder?.invoke(options)

        navController.navigate(action, navOptions = options.build())
    }

    fun setNavGraph(@NavigationRes graphResId: Int) {
        if (navController.graph.id != graphResId)
            navController.setGraph(graphResId)
    }

    fun setNavGraph(@NavigationRes graphResId: Int, startDestination: Int, startDestinationArgs: Bundle? = null) {
        val inflater = navController.navInflater
        val graph = inflater.inflate(graphResId)
        graph.setStartDestination(startDestination)
        navController.setGraph(graph, startDestinationArgs = startDestinationArgs)
    }
}