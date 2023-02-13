package com.isidroid.b21.domain.use_case

import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeUseCase @Inject constructor() {
    fun start() = flow {
        emit(true)
    }
}