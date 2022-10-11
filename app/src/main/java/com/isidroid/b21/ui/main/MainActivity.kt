package com.isidroid.b21.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import com.isidroid.b21.R
import com.isidroid.b21.databinding.ActivityMainBinding
import com.isidroid.core.ext.findNavController
import com.isidroid.core.ext.updateStatusBarColorExt
import com.isidroid.b21.ui.home.HomeFragment
import com.isidroid.b21.utils.base.BindActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BindActivity(), MainView {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<MainViewModel>()
    private val navController by lazy { findNavController(R.id.nav_host_fragment) }

    override val popupToDestinationId: Int?
        get() = when (currentFragment) {
            is HomeFragment -> R.id.fragmentHome
            else -> null
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
            .setKeepOnScreenCondition { viewModel.isInitInProgress }

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        updateStatusBarColor(colorRes = R.color.toolbar_color, isLightStatusBarIcons = false)
    }

    // MainView
    override fun navigateTo(action: NavDirections, clearBackStack: Boolean) {
        val options = if (clearBackStack)
            with(NavOptions.Builder()) {
                popupToDestinationId?.also { setPopUpTo(it, inclusive = true) }
                build()
            }
        else
            null

        navController.navigate(action, navOptions = options)
    }

    override fun updateStatusBarColor(colorRes: Int, isLightStatusBarIcons: Boolean) {
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = isLightStatusBarIcons
        updateStatusBarColorExt(colorRes)
    }
}