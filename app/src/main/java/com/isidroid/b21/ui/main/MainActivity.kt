package com.isidroid.b21.ui.main

import android.content.res.Configuration
import android.os.Bundle
import android.view.ViewGroup
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.color.MaterialColors
import com.isidroid.b21.R
import com.isidroid.b21.databinding.ActivityMainBinding
import com.isidroid.b21.utils.base.BindActivity
import com.isidroid.core.ext.color
import com.isidroid.core.ext.findNavController
import com.isidroid.core.ext.visible
import com.isidroid.core.ui.*
import com.isidroid.core.utils.KeyboardVisibilityListener
import com.isidroid.core.utils.WindowInsetListener
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : BindActivity(), MainView, NavigationListener, BottomNavigationListener {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private var isKeyboardVisible = false
        set(value) {
            (currentFragment as? KeyboardVisibilityListener)?.onKeyboardVisibilityChanged(value)
            field = value
        }

    override val navController by lazy { findNavController(R.id.nav_host_fragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition { false }
        WindowCompat.setDecorFitsSystemWindows(window, false)

        super.onCreate(savedInstanceState)
        updateStatusBarColor()

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createWindowInset()
        createBaseView()
        onCreateViewModel()

        navController.addOnDestinationChangedListener { controller, _, _ ->
            Timber.i("navigationBackQueue=${controller.backQueue.mapNotNull { it.destination.label }}")
        }
    }

    private fun createWindowInset() {
        with(binding) {
            ViewCompat.setOnApplyWindowInsetsListener(constraintLayout) { view, insets ->
                val ime = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom

                val systemBarInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    leftMargin = systemBarInsets.left
                    rightMargin = systemBarInsets.right
                    bottomMargin = if (ime > 0) ime else systemBarInsets.bottom
                }

                topInset = systemBarInsets.top
                toolbar.updateLayoutParams<ViewGroup.MarginLayoutParams> { topMargin = systemBarInsets.top }

                if (ime > 0 && !isKeyboardVisible)
                    isKeyboardVisible = true
                else if (ime == 0 && isKeyboardVisible)
                    isKeyboardVisible = false

                (currentFragment as? WindowInsetListener)?.onInsetChanged(start = systemBarInsets.left, top = systemBarInsets.top, end = systemBarInsets.right, bottom = systemBarInsets.bottom)

                WindowInsetsCompat.CONSUMED
            }
        }
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

    // MainView
    override fun updateProgress(isProgress: Boolean) {
        binding.progressBar.visible(isProgress)
    }
}