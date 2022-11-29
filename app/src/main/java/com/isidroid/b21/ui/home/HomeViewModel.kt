package com.isidroid.b21.ui.home

import android.content.Context
import android.net.Uri
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.isidroid.b21.domain.usecase.HomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
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

    fun upload(context: Context, uri: Uri) {
        viewModelScope.launch {
            useCase.upload(context, uri)
                .flowOn(Dispatchers.IO)
                .catch { _viewState.value = State.OnError(it) }
                .collect { }
        }
    }
}