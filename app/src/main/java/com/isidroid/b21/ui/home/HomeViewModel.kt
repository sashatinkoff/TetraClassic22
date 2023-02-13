package com.isidroid.b21.ui.home

import androidx.lifecycle.*
import com.isidroid.b21.domain.use_case.HomeUseCase
import com.isidroid.core.ext.catchTimber
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: HomeUseCase
) : ViewModel() {
    private val _viewState = MutableStateFlow<UiState?>(null)
    val viewState = _viewState.asStateFlow()

    fun start() {
        viewModelScope.launch {
            useCase.start()
                .catchTimber { }
                .collect()
        }
    }
}