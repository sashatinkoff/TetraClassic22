package com.isidroid.b21.ui.main

import android.content.res.Configuration
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.annotation.ColorRes
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.ui.NavigationUI
import com.isidroid.b21.R
import com.isidroid.b21.databinding.ActivityMainBinding
import com.isidroid.b21.utils.base.BindActivity
import com.isidroid.core.ext.color
import com.isidroid.core.ext.findNavController
import com.isidroid.core.ext.visible
import com.isidroid.core.ui.AppBarListener
import com.isidroid.core.ui.BottomNavigationListener
import com.isidroid.core.ui.NavigationListener
import com.isidroid.core.ui.StatusColorListener
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : BindActivity(), MainView, NavigationListener, StatusColorListener, BottomNavigationListener {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MainViewModel>()

    override var topInset: Int = 0
    override val isNightMode: Boolean
        get() {
            val darkModeFlag = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            return darkModeFlag == Configuration.UI_MODE_NIGHT_YES
        }
    override val navController by lazy { findNavController(R.id.nav_host_fragment) }
    override val statusBarColorRes: Int = android.R.color.transparent
    override val navigationBarColorRes: Int = android.R.color.transparent
    override val isLightStatusBarIcons: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition { viewModel.isInitInProgress }
        WindowCompat.setDecorFitsSystemWindows(window, false)

        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createBaseView()
        onCreateViewModel()

        with(binding) {
            ViewCompat.setOnApplyWindowInsetsListener(constraintLayout) { view, insets ->
                val ime = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom

                val insets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    leftMargin = insets.left
                    rightMargin = insets.right
                    bottomMargin = if (ime > 0) ime else insets.bottom
                }

                topInset = insets.top
                toolbar.updateLayoutParams<ViewGroup.MarginLayoutParams> { topMargin = insets.top }

                WindowInsetsCompat.CONSUMED
            }
        }

        navController.addOnDestinationChangedListener { controller, _, _ ->
            Timber.i("navigationBackQueue=${controller.backQueue.mapNotNull { it.destination.label }}")
        }

        updateStatusBarColor()
    }

    override fun createAppBar() {
        with(binding) {
            NavigationUI.setupWithNavController(bottomNavigationView, navController)
        }
    }

    override fun onFragmentResumed(fr: Fragment) {
        super.onFragmentResumed(fr)

        with(binding) {
            bottomNavigationView.visible(fr is BottomNavigationListener)
            toolbar.visible(fr is AppBarListener)
        }

        (fr as? AppBarListener)?.createToolbar(binding.toolbar, navController)

        (fr as? StatusColorListener ?: this).also {
            updateStatusBarColor(
                statusBarColorRes = it.statusBarColorRes,
                navigationBarColorRes = it.navigationBarColorRes,
                isLightStatusBarIcons = it.isLightStatusBarIcons
            )
        }
    }

    // StatusColorListener
    override fun updateStatusBarColor(@ColorRes statusBarColorRes: Int, @ColorRes navigationBarColorRes: Int, isLightStatusBarIcons: Boolean) {
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = isLightStatusBarIcons
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightNavigationBars = isLightStatusBarIcons

        window.navigationBarColor = color(navigationBarColorRes)
        window.statusBarColor = color(statusBarColorRes)
    }

    // MainView
    override fun updateProgress(isProgress: Boolean) {
        binding.progressBar.visible(isProgress)
    }
}