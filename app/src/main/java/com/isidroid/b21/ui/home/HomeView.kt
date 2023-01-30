package com.isidroid.b21.ui.home

interface HomeView {
    fun onEvent(logs: List<String>)
    fun onStats(liveJournalCount: Int, liveInternetCount: Int, updatedAt: String, liveJournalDownloaded: Int)
}