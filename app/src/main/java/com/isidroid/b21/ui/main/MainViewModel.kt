package com.isidroid.b21.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.isidroid.b21.domain.usecase.SessionUseCase
import com.isidroid.b21.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sessionUseCase: SessionUseCase
) : BaseViewModel() {
    private val _viewState = MutableLiveData<State>(State.Empty)
    val viewState: LiveData<State> get() = _viewState

    var isInitInProgress = true

    init {
        io(
            coroutineContext = Dispatchers.Default,
            doWork = { sessionUseCase.create() },
            onSuccess = { isInitInProgress = false },
            onFail = {
                isInitInProgress = false
                _viewState.value = State.OnError(it)
            }
        )
    }
}