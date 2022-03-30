package com.isidroid.b21.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel() {

    protected fun <T> io(
        doBefore: (() -> Boolean)? = null,
        onLoading: (() -> Unit)? = null,
        onIdle: (() -> Unit)? = null,
        doWork: () -> Flow<ResultData<T>>,
        onFail: ((Throwable) -> Unit)? = null,
        onSuccess: ((T) -> Unit)? = null,
        coroutineContext: CoroutineContext = Dispatchers.IO
    ) {
        if (doBefore?.invoke() == false) return
        viewModelScope.launch {
            doWork()
                .flowOn(coroutineContext)
                .catch {
                    Timber.e(it)
                    onFail?.invoke(it)
                }
                .collect { result ->
                    when (result) {
                        is ResultData.Idle -> onIdle?.invoke()
                        is ResultData.Loading -> onLoading?.invoke()
                        is ResultData.Success -> onSuccess?.invoke(result.data)
                        is ResultData.Error -> onFail?.invoke(result.exception)
                    }
                }
        }
    }

}