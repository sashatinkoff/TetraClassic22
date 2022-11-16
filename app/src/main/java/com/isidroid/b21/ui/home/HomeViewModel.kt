package com.isidroid.b21.ui.home

import androidx.lifecycle.*
import com.isidroid.b21.domain.usecase.HomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: HomeUseCase
) : ViewModel() {
    private val _viewState = MutableLiveData<State>()
    val viewState: LiveData<State> get() = _viewState

    fun getCharacters() {
        viewModelScope.launch {
            useCase.getCharacters()
                .catch { _viewState.value = State.OnError(it) }
                .collect { _viewState.value = State.OnListReady(it.data.list, it.data.total) }
        }
    }

    fun loadCharacter(id: Int) {
        viewModelScope.launch {
            useCase.loadCharacter(id)
                .catch { _viewState.value = State.OnError(it) }
                .collect { _viewState.value = State.OnCharacterReady(it.data) }
        }
    }
}