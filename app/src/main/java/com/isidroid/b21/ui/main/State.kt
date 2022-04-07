package com.isidroid.b21.ui.main

sealed class State {
    object Empty : State()
    data class OnSuccess(val name: String) : State()
    data class OnError(val t: Throwable) : State()
}