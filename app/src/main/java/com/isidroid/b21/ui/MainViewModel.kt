package com.isidroid.b21.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.isidroid.b21.domain.usecase.SessionUseCase
import com.isidroid.b21.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sessionUseCase: SessionUseCase
) : BaseViewModel() {
    private val _viewState = MutableLiveData<State>(State.Empty)
    val viewState: LiveData<State> get() = _viewState
}

sealed class State {
    object OnLoading : State()
    object Empty : State()
    data class OnSuccess(val name: String) : State()
    data class OnError(val t: Throwable) : State()
}