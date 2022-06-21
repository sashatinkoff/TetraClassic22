package com.isidroid.b21.ui.main

import androidx.navigation.NavDirections

interface MainView {
    val popupToDestinationId: Int?

    fun navigateTo(action: NavDirections, clearBackStack: Boolean = false)
}