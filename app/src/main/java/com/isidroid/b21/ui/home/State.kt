package com.isidroid.b21.ui.home

sealed class State {
    data class OnError(val t: Throwable) : State()
    data class OnEvent(val logs: List<String>): State()
    data class OnStats(val liveInternetCount: Int, val liveJournalCount: Int, val updatedAt: String, val liveJournalDownloaded: Int) : State()

    object Empty : State()
}
