package com.isidroid.b21.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.isidroid.b21.domain.usecase.HomeUseCase
import com.isidroid.b21.ext.formatDateTime
import com.isidroid.core.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val useCase: HomeUseCase
) : BaseViewModel() {
    private val _viewState = MutableLiveData<State>()
    val viewState: LiveData<State> get() = _viewState

    fun sendMessages() {
        io(
            doWork = { useCase.sendMessages() },
            onSuccess = { _viewState.value = it },
            onFail = { _viewState.value = State.OnError(it) }
        )
    }
}