package com.isidroid.b21.ui.home

import com.isidroid.b21.ui.home.adapter.Item

sealed interface UiState {
    data class Loading(val isLoading: Boolean) : UiState
    data class Error(val t: Throwable) : UiState
    data class Data(val items: List<Item>, val hasMore: Boolean): UiState
}
