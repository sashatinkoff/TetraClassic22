package com.isidroid.b21.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.isidroid.b21.databinding.FragmentHomeBinding
import com.isidroid.b21.utils.base.BindFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BindFragment(), HomeView {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val args: HomeFragmentArgs by navArgs()
    private val viewModel by viewModels<HomeViewModel>()

    private val documentPdfContract = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) {
        viewModel.createPdf(it!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun createForm() {
        with(binding) {
            button.setOnClickListener {
                binding.textView.text = ""
                viewModel.create()
            }
            buttonStop.setOnClickListener { viewModel.stop() }
            buttonPdf.setOnClickListener { documentPdfContract.launch(null) }
            buttonLiveinternet.setOnClickListener { viewModel.liveInternet() }
        }
    }

    override fun onCreateViewModel() {
        viewModel.viewState.observe(this) { state ->
            when (state) {
                is State.OnError -> showError(state.t)
                State.Empty -> {}
                is State.OnEvent -> onEvent(state.logs)
            }
        }
    }

    override fun onEvent(logs: List<String>) {
        binding.textView.text = buildString {
            for (s in logs) {
                append("- $s")
                append("\n")
            }
        }
    }

}