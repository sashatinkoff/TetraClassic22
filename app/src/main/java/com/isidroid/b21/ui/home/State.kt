package com.isidroid.b21.ui.home

import com.isidroid.b21.domain.model.Post

sealed class State {
    data class OnError(val t: Throwable) : State()
    data class OnEvent(val logs: List<String>): State()

    object Empty : State()
}
