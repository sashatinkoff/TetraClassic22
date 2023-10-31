package com.isidroid.b21.ui.home

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.isidroid.b21.databinding.FragmentHomeBinding
import com.isidroid.b21.utils.base.BindFragment
import com.isidroid.core.ext.enable
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : BindFragment(), HomeView {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<HomeViewModel>()

    private val documentPdfContract = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) {
        it ?: return@registerForActivityResult
        viewModel.createPdf(it, name = binding.input.text.toString())
    }

    private val documentHashTagsContract = registerForActivityResult(ActivityResultContracts.OpenDocument()) {
        it ?: return@registerForActivityResult
        viewModel.createHashTags(
            it,
            showCounters = binding.checkboxHashTagsWithCounter.isChecked,
            isSingleLine = binding.checkboxSingleLIne.isChecked,
            isSplitByLetter = binding.checkboxSplitByLetter.isChecked && binding.checkboxSingleLIne.isChecked
        )
    }

    override fun createForm() {
        with(binding) {
            checkboxSplitByLetter.isChecked = true
            checkboxSingleLIne.isChecked = true

            checkboxSplitByLetter.enable(true)
            checkboxSingleLIne.setOnCheckedChangeListener { _, isChecked -> checkboxSplitByLetter.enable(isChecked) }
        }
    }

    override fun onReady() {
        binding.button.setOnClickListener { documentPdfContract.launch(null) }
        binding.buttonHashTags.setOnClickListener { documentHashTagsContract.launch(arrayOf("application/json", "text/*")) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override suspend fun onCreateViewModel() {
        viewModel.viewState.collect { state ->
            when (state) {
                UiState.Complete -> Toast.makeText(requireContext(), "complete", Toast.LENGTH_SHORT).show()
                is UiState.Error -> showError(state.t)
                is UiState.Progress -> {
                    binding.textView.text = buildString { append("${state.current}/${state.max}") }
                    binding.progressBar.setProgressCompat(state.current, true)
                    binding.progressBar.max = state.max
                }

                is UiState.CompleteHashTags -> completeHashTags(state.content)
                is UiState.Loading -> TODO()
                null -> {}
            }
        }
    }

    private fun completeHashTags(content: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val clipboard = requireActivity().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Telegram hash tags", content)
            clipboard.setPrimaryClip(clip)
        } else {

        }


        binding.contentTextView.text = content
    }
}