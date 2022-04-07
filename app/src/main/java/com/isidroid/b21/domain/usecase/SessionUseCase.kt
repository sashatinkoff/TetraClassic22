package com.isidroid.b21.domain.usecase

import com.isidroid.b21.domain.repository.SessionRepository
import com.isidroid.b21.utils.ResultData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import java.util.*
import javax.inject.Inject

class SessionUseCase @Inject constructor(
    private val repository: SessionRepository
) {
    fun create() = flow {
        emit(ResultData.Loading)
        delay(2_500)
        emit(
            ResultData.Success(
                UUID.randomUUID().toString().take(5) + " on ${Thread.currentThread().name}"
            )
        )
    }
}