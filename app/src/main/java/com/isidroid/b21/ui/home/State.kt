package com.isidroid.b21.ui.home

import com.isidroid.b21.domain.model.Post

sealed class State {
    data class OnError(val t: Throwable) : State()

    data class OnProgress(
        val filesCount: Int,
        val currentFile: Int,
        val postsInFileCount: Int,
        val currentPost: Int,
        val post: Post
    ) : State()

    object OnComplete : State()
}
