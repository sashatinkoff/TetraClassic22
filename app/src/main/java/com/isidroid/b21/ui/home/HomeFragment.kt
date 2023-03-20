package com.isidroid.b21.ui.home

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.isidroid.b21.App
import com.isidroid.b21.databinding.FragmentHomeBinding
import com.isidroid.b21.utils.base.BindFragment
import com.isidroid.core.ext.randomColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : BindFragment(), HomeView {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val args: HomeFragmentArgs by navArgs()
    private val viewModel by viewModels<HomeViewModel>()

    private val documentPdfContract = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) {
        viewModel.createPdf(it!!)
    }

    private val saveJsonContract = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) {
        binding.textView.text = ""
        viewModel.saveLjJson(it!!)
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
            buttonLiveJournalJson.setOnClickListener {
//                saveJsonContract.launch(null)
                viewModel.storeLiveJournalDb()
            }
        }
    }

    override fun onCreateViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.viewState.collect { state ->
                    when (state) {
                        is State.OnError -> binding.statusTextView.text = "${state.t}"
                        State.Empty -> {}
                        is State.OnEvent -> onEvent(state.logs)
                        is State.OnStats -> onStats(state.liveJournalCount, state.liveInternetCount, state.updatedAt, state.liveJournalDownloaded)
                    }
                }
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

    override fun onStats(liveJournalCount: Int, liveInternetCount: Int, updatedAt: String, liveJournalDownloaded: Int) {
        binding.statusTextView.text = buildString {
            append(updatedAt)
            append("\n")
            append("liveJournalCount=$liveJournalCount/$liveJournalDownloaded")
            append("\n")
            append("liveInternetCount=$liveInternetCount")
        }
    }

    override fun onReady() {
        super.onReady()

        if (view2 == null) {
            view2 = binding.root

            view2?.setBackgroundColor(randomColor())
        }

    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var view2: View? = null
    }

}