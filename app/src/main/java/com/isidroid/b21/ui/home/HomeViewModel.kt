package com.isidroid.b21.ui.home

import android.net.Uri
import androidx.lifecycle.*
import com.isidroid.b21.App
import com.isidroid.b21.domain.use_case.HomeUseCase
import com.isidroid.core.ext.copyToPublicFolder
import com.isidroid.core.ext.saveString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.util.*
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
                .collect { _viewState.value = State.OnContent(it) }
        }
    }
}