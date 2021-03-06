package com.isidroid.b21.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.isidroid.b21.databinding.FragmentHomeBinding
import com.isidroid.b21.ext.date
import com.isidroid.b21.utils.base.BindFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

@AndroidEntryPoint
class HomeFragment : BindFragment(), HomeView {
    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private val args: HomeFragmentArgs by navArgs()
    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = binding.root

    override fun onReady() {
    }

}