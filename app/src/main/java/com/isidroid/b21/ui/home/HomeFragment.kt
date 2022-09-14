package com.isidroid.b21.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.isidroid.b21.databinding.FragmentHomeBinding
import com.isidroid.b21.domain.model.Post
import com.isidroid.b21.ext.formatDateTime
import com.isidroid.b21.utils.base.BindFragment
import com.isidroid.core.ext.enable
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BindFragment(), HomeView {
    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private val args: HomeFragmentArgs by navArgs()
    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = binding.root

    override fun createForm() {
        binding.button2.setOnClickListener {
            it.enable(false)
            viewModel.sendMessages()
        }

        binding.buttonStop.setOnClickListener {
            Runtime.getRuntime().exit(0)
        }
    }

    override fun onCreateViewModel() {
        viewModel.viewState.observe(this) { state ->
            when (state) {
                is State.OnError -> showError(state.t)
                State.OnComplete -> Toast.makeText(requireActivity(), "Done", Toast.LENGTH_SHORT).show()
                is State.OnProgress -> onProgress(state.currentFile, state.filesCount, state.currentPost, state.postsInFileCount, state.post)
            }
        }
    }

    override fun onProgress(currentFile: Int, filesCount: Int, currentPost: Int, postsInFileCount: Int, post: Post) {
        with(binding) {
            progressBarGlobal.progress = currentFile + 1
            progressBarGlobal.max = filesCount

            globalTextView.text = "files: ${progressBarGlobal.progress}/${progressBarGlobal.max}"

            progressBar.progress = currentPost + 1
            progressBar.max = postsInFileCount

            textView.text = "posts: ${progressBar.progress}/${progressBar.max}"

            currentPostTextView.text = "${post.createdAt.formatDateTime}"
        }
    }
}
