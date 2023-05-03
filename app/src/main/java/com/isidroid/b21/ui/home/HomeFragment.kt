package com.isidroid.b21.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.isidroid.b21.R
import com.isidroid.b21.databinding.FragmentHomeBinding
import com.isidroid.b21.utils.base.BindFragment
import com.isidroid.core.ui.AppBarListener
import com.isidroid.core.view.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BindFragment(R.layout.fragment_home), HomeView, AppBarListener {
    private val viewModel by viewModels<HomeViewModel>()
    private val binding by viewBinding<FragmentHomeBinding>()


    override fun onReady() {
    }

    override fun createForm() {
    }
}
