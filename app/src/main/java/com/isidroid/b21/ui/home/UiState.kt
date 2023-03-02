package com.isidroid.b21.ui.home

sealed interface UiState {
    data class Loading(val isLoading: Boolean) : UiState
    data class Error(val t: Throwable) : UiState

    object Complete: UiState
    data class Progress(val current: Int, val max: Int) : UiState
}
