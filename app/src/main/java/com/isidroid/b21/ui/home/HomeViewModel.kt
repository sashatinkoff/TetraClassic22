package com.isidroid.b21.ui.home

import androidx.lifecycle.*
import com.isidroid.b21.domain.use_case.HomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val useCase: HomeUseCase
) : ViewModel() {
    private val _viewState = MutableLiveData<State>()
    val viewState: LiveData<State> get() = _viewState

    fun start() {
        viewModelScope.launch {
            useCase.start()
                .catch { Timber.e(it) }
                .collect()
        }
    }

}