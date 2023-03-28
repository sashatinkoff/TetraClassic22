package com.isidroid.b21.ui.main

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import com.isidroid.b21.R
import com.isidroid.b21.data.source.settings.Settings
import com.isidroid.b21.databinding.ActivityMainBinding
import com.isidroid.b21.ui.AppBarListener
import com.isidroid.core.ext.findNavController
import com.isidroid.b21.utils.base.BindActivity
import com.isidroid.core.ext.color
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BindActivity(), MainView, AppBarListener {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<MainViewModel>()
    private val navController by lazy { findNavController(R.id.nav_host_fragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
            .setKeepOnScreenCondition { viewModel.isInitInProgress }

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        updateStatusBarColor(isLightStatusBarIcons = false, colorRes = android.R.color.black)
    }

    override fun onFragmentResumed(fr: Fragment) {
        super.onFragmentResumed(fr)

        (fr as? AppBarListener ?: this@MainActivity).createToolbar(binding.toolbar, navController)
    }

    // MainView
    override fun navigateTo(action: NavDirections, optionsBuilder: ((NavOptions.Builder) -> Unit)?) {
        val options = NavOptions.Builder()
            .setEnterAnim(R.anim.nav_default_enter_anim)
            .setExitAnim(R.anim.nav_default_exit_anim)
            .setPopEnterAnim(R.anim.nav_default_pop_enter_anim)
            .setPopExitAnim(R.anim.nav_default_pop_exit_anim)

        optionsBuilder?.invoke(options)
        navController.navigate(action, navOptions = options.build())
    }

    override fun updateStatusBarColor(colorRes: Int, isLightStatusBarIcons: Boolean) {
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = isLightStatusBarIcons
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightNavigationBars = isLightStatusBarIcons

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        window.navigationBarColor = color(colorRes)
        window.statusBarColor = color(colorRes)
    }
}