package com.isidroid.b21.ui.home

import android.net.Uri
import androidx.lifecycle.*
import com.isidroid.b21.domain.use_case.HomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: HomeUseCase,
    private val state: SavedStateHandle
) : ViewModel() {
    private val _viewState = MutableLiveData<State>()
    val viewState: LiveData<State> get() = _viewState


    fun create() {
        viewModelScope.launch {
            useCase.start()
                .flowOn(Dispatchers.IO)
                .catch {
                    Timber.e(it)
                    _viewState.value = State.OnError(it)
                }
                .collect {
                    _viewState.value = when (it) {
                        is HomeUseCase.Result.OnLoading -> State.OnLoading(it.url)
                        is HomeUseCase.Result.OnPostFoundLocal -> State.OnPostFoundLocal(it.post)
                        is HomeUseCase.Result.OnPostSaved -> State.OnContent(it.post)
                        else -> State.Empty
                    }
                }
        }
    }

    fun stop() {
        useCase.stop()
    }

    fun createPdf(uri: Uri) {
        viewModelScope.launch {
            useCase.createPdf(uri)
                .flowOn(Dispatchers.IO)
                .catch { Timber.e(it) }
                .collect {
                    _viewState.value = State.OnPdfCreated
                }

        }
    }

    fun liveinternet() {
        viewModelScope.launch {
            useCase.liveinternet()
                .flowOn(Dispatchers.IO)
                .catch { Timber.e(it) }
                .collect { _viewState.value = State.OnLiveInternet }
        }
    }
}