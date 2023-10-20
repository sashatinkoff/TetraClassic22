package com.isidroid.b21.ui.home

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import com.google.android.material.appbar.MaterialToolbar
import com.isidroid.b21.databinding.FragmentHomeBinding
import com.isidroid.b21.utils.base.BindFragment
import com.isidroid.core.ext.visible
import com.isidroid.core.ui.AppBarListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BindFragment(), HomeView {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<HomeViewModel>()

    private val documentPdfContract = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) {
        viewModel.createPdf(it!!, name = binding.input.text.toString())
    }

    private val documentHashTagsContract = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) {
        viewModel.createHashTags(
            it!!,
            moreThanOne = binding.checkboxHashTagsMoreOne.isChecked,
            showCounters = binding.checkboxHashTagsWithCounter.isChecked,
            alreadyHaveHashTags = binding.checkboxAlreadyHaveHashtagsPost.isChecked,
            saveHashTagsInFile = binding.checkboxSaveHashTagsInFile.isChecked
        )
    }

    override fun onReady() {
        binding.button.setOnClickListener { documentPdfContract.launch(null) }
        binding.buttonHashTags.setOnClickListener { documentHashTagsContract.launch(null) }
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

                is UiState.CompleteHashTags -> completeHashTags(state.fileName, state.content)
                is UiState.Loading -> TODO()
                null -> {}
            }
        }
    }

    private fun completeHashTags(fileName: String?, content: String) {
        if (fileName != null)
            Toast.makeText(activity, "Content saved in $fileName", Toast.LENGTH_SHORT).show()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val clipboard = requireActivity().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Telegram hash tags", content)
            clipboard.setPrimaryClip(clip)
        } else {

        }


        binding.contentTextView.text = content
    }
}