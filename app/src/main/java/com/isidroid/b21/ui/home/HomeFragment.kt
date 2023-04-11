package com.isidroid.b21.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.google.android.material.appbar.MaterialToolbar
import com.isidroid.b21.R
import com.isidroid.b21.databinding.FragmentHomeBinding
import com.isidroid.b21.utils.base.BindFragment
import com.isidroid.core.ext.visible
import com.isidroid.core.ui.AppBarListener
import com.isidroid.core.view.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

@AndroidEntryPoint
class HomeFragment : BindFragment(R.layout.fragment_home), HomeView, AppBarListener {
    private val viewModel by viewModels<HomeViewModel>()
    private val binding by viewBinding<FragmentHomeBinding>()

}
