package com.isidroid.b21.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import com.google.android.material.appbar.MaterialToolbar
import com.isidroid.b21.data.source.settings.Settings
import com.isidroid.b21.databinding.FragmentHomeBinding
import com.isidroid.b21.utils.base.BindFragment
import com.isidroid.core.ext.visible
import com.isidroid.core.ui.AppBarListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BindFragment(), HomeView, AppBarListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun createToolbar(toolbar: MaterialToolbar, navController: NavController) {
        super.createToolbar(toolbar, navController)
        toolbar.visible(true)
        toolbar.title = "Hello Sample World"
    }

    override fun createForm() {
    }

    override fun onReady() {
        viewModel.makePreview(
            arrayOf(
                "https://teachershelp.ru/symbols-of-russia-official-and-non-official/",
                "https://www.eg.ru/showbusiness/3057082-koncert-maksim-v-nijnem-novgorode/"
            )
        )
    }
}