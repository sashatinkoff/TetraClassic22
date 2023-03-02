package com.isidroid.core.ext

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import timber.log.Timber

fun <T> Flow<T>.catchTimber(action: suspend FlowCollector<T>.(Throwable) -> Unit): Flow<T> = catch { error ->
    Timber.e(error)
    action(error)
}