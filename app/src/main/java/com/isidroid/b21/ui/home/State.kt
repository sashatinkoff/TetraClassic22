package com.isidroid.b21.ui.home

import com.isidroid.b21.domain.model.Post

sealed class State {
    data class OnError(val t: Throwable) : State()
    data class OnContent(val post: Post) : State()
}
