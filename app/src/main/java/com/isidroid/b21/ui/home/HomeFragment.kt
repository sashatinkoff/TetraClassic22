package com.isidroid.b21.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import androidx.core.text.scale
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.isidroid.b21.databinding.FragmentHomeBinding
import com.isidroid.b21.utils.base.BindFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : BindFragment(), HomeView {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val args: HomeFragmentArgs by navArgs()
    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun createForm() {
        with(binding) {
            buttonStart.setOnClickListener { viewModel.startListen() }
            buttonStop.setOnClickListener { viewModel.stopListen() }
            buttonDetails.setOnClickListener { navigateTo(HomeFragmentDirections.openDetails()) }
        }
    }

    override fun onReady() {
    }

    override fun onCreateViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.startListen().collect {
                    onMessage(it)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        Timber.i("sdfsdfsdf onPause")
    }

    override fun onMessage(message: String) {
        with(binding) {
            val oldMessage = textView.text.toString()

            textView.text = buildSpannedString {
                append(message)
                append("\n")
                color(Color.GRAY) {
                    append(oldMessage)
                }
            }
        }
    }
}