package com.isidroid.b21.ui.home

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.isidroid.b21.databinding.FragmentHomeBinding
import com.isidroid.b21.utils.base.BindFragment
import com.isidroid.core.ext.alert
import com.isidroid.core.ext.randomColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BindFragment(), HomeView {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<HomeViewModel>()
    private var pdfUri: Uri? = null

    private val documentPdfContract = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) {
        pdfUri = it
        pdfImagesContract.launch(null)
    }

    private val pdfImagesContract = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) {
        viewModel.createPdf(pdfUri = pdfUri!!, imagesUri = it)
    }

    private val saveJsonContract = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) {
        binding.textView.text = ""
        viewModel.saveLjJson(it!!)
    }

    private val downloadPicturesContract = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) {
        viewModel.downloadPictures(it)
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
            buttonPdf.setOnClickListener {
                pdfUri = null

                activity?.alert(
                    message = "Select pdf folder",
                    positiveRes = android.R.string.ok,
                    onPositive = { documentPdfContract.launch(null) }
                )
            }
            buttonLiveinternet.setOnClickListener { viewModel.liveInternet() }
            buttonLiveJournalJson.setOnClickListener {
//                saveJsonContract.launch(null)
                viewModel.storeLiveJournalDb()
            }

            buttonSaveLjToJson.setOnClickListener { saveJsonContract.launch(null) }
            buttonLoadImages.setOnClickListener {
                downloadPicturesContract.launch(null)
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

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var view2: View? = null
    }

}