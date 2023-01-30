package com.isidroid.b21.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.text.HtmlCompat
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.isidroid.b21.App
import com.isidroid.b21.databinding.FragmentHomeBinding
import com.isidroid.b21.domain.model.Post
import com.isidroid.b21.utils.base.BindFragment
import com.isidroid.core.ext.copy
import com.isidroid.core.ext.copyToPublicFolder
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class HomeFragment : BindFragment(), HomeView {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val args: HomeFragmentArgs by navArgs()
    private val viewModel by viewModels<HomeViewModel>()
    private val contract = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) {
        val file = File(App.instance.cacheDir, "download_file.html")
        file.copyToPublicFolder(requireContext(), "download_file.html", it!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun createForm() {
        with(binding) {
            button.setOnClickListener { viewModel.create() }
            buttonStop.setOnClickListener { viewModel.stop() }
        }
    }

    override fun onCreateViewModel() {
        viewModel.viewState.observe(this) { state ->
            when (state) {
                is State.OnError -> showError(state.t)
                is State.OnContent -> onContent(state.post)
                State.Empty -> {}
                is State.OnLoading -> onLoading(state.url)
                is State.OnPostFoundLocal -> onPostFoundLocal(state.post)
            }
        }
    }

    override fun onContent(post: Post) {
        appendText("Post ${post.title} saved")
    }

    override fun onLoading(url: String) {
        appendText("$url is loading")
    }

    override fun onPostFoundLocal(post: Post) {
        appendText("${post.title} found in local database")
    }

    override fun appendText(text: String) {
        with(binding) {
            val content = buildString {
                append("- $text")
                append("\n")
                append(textView.text.toString())
                append("\n")
            }

            textView.text = content
        }
    }
}