package com.isidroid.b21.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.isidroid.b21.GlideApp
import com.isidroid.b21.databinding.FragmentHomeBinding
import com.isidroid.b21.domain.model.CartCharacter
import com.isidroid.b21.utils.base.BindFragment
import com.isidroid.core.ext.enable
import com.isidroid.core.ext.visible
import dagger.hilt.android.AndroidEntryPoint

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
            buttonCharacter.enable(false)
            button.setOnClickListener {
                progressBar.visible(true)
                viewModel.getCharacters()
            }
        }
    }

    override fun onReady() {
        binding.progressBar.isInvisible = true
    }

    override fun showError(t: Throwable?, isCritical: Boolean, buttonTitle: String?, onButtonClick: (() -> Unit)?) {
        super.showError(t, isCritical, buttonTitle, onButtonClick)
        binding.errorView.text = buildString { append("${t?.message}") }
    }

    override fun onCreateViewModel() {
        viewModel.viewState.observe(this) { state ->
            when (state) {
                is State.OnError -> showError(state.t)
                is State.OnListReady -> onListReady(state.list, state.total)
                is State.OnCharacterReady -> onCharacterReady(state.character)
            }
        }
    }

    override fun onListReady(list: List<CartCharacter>, total: Int) {
        with(binding) {
            val character = list.random()
            button.enable(false)

            progressBar.visible(false)
            infoView.text = buildString { append("Total: $total") }
            buttonCharacter.enable(true)
            buttonCharacter.text = buildString { append("Load ${character.name}") }

            buttonCharacter.setOnClickListener {
                buttonCharacter.enable(false)
                progressBar.visible(true)
                viewModel.loadCharacter(character.id)
            }
        }
    }

    override fun onCharacterReady(character: CartCharacter) {
        with(binding) {
            progressBar.visible(false)
            GlideApp.with(imageView).load(character.image).into(imageView)

            binding.errorView.text = ""
            infoView.text = buildString { append("character: ${character.id}, name=${character.name}") }
            button.enable(true)
        }
    }
}