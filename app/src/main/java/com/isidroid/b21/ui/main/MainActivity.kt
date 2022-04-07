package com.isidroid.b21.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.isidroid.b21.databinding.ActivityMainBinding
import com.isidroid.b21.ui.main.MainView
import com.isidroid.b21.ui.main.MainViewModel
import com.isidroid.b21.utils.base.BindActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BindActivity(), MainView {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
            .setKeepOnScreenCondition { viewModel.isInitInProgress }

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    // Let's init UI in following methods
    override fun createAppBar() {}
    override fun createForm() {}
    override fun createAdapter() {}

    // MainView
}