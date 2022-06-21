package com.isidroid.b21.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import com.isidroid.b21.R
import com.isidroid.b21.databinding.ActivityMainBinding
import com.isidroid.b21.ext.findNavController
import com.isidroid.b21.ui.home.HomeFragment
import com.isidroid.b21.ui.main.MainView
import com.isidroid.b21.ui.main.MainViewModel
import com.isidroid.b21.utils.base.BindActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

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
}