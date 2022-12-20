package com.isidroid.b21.ui.home

sealed class State {
    data class OnError(val t: Throwable): State()
    data class OnMessage(val message: String): State()
    object Empty: State()
}
