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
    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private val args: HomeFragmentArgs by navArgs()
    private val viewModel by viewModels<HomeViewModel>()
    private val documentsContractWriter = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) {
        viewModel.createAndSave(it)
    }

    private val documentsContractReader = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) {
        viewModel.read(it)
    }

    private val documentPdfContract = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) {
        viewModel.createPdf(it!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = binding.root

    override fun onReady() {
//        binding.button.setOnClickListener { documentsContractWriter.launch(null) }
//        binding.buttonRead.setOnClickListener { documentsContractReader.launch(null) }
        binding.button.setOnClickListener { documentPdfContract.launch(null)}
    }
}