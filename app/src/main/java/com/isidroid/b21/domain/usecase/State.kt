package com.isidroid.b21.domain.usecase

sealed class State {
    data class OnProgress(val current: Int, val max: Int): State()
    object OnComplete: State()
}