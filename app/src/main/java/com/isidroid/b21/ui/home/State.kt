package com.isidroid.b21.ui.home

import com.isidroid.b21.domain.model.Post
import com.isidroid.b21.domain.use_case.HomeUseCase

sealed class State {
    data class OnError(val t: Throwable) : State()
    data class OnContent(val post: Post) : State()
    data class OnLoading(val url: String) : State()
    data class OnPostFoundLocal(val post: Post) : State()


    object Empty : State()
    object OnPdfCreated: State()
    object OnLiveInternet: State()
}
