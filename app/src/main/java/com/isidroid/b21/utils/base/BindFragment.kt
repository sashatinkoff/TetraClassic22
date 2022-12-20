package com.isidroid.b21.utils.base

import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import com.isidroid.b21.ui.NavigationListener
import com.isidroid.core.ui.CoreBindFragment

abstract class BindFragment : CoreBindFragment(), NavigationListener {
    override val navController get() = (activity as NavigationListener).navController

    override fun navigateTo(action: NavDirections, optionsBuilder: ((NavOptions.Builder) -> Unit)?) {
        (activity as? NavigationListener)?.navigateTo(action, optionsBuilder)
    }

    override fun setNavGraph(graphResId: Int): NavigationListener {
        return (activity as NavigationListener).setNavGraph(graphResId)
    }
}