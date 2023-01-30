package com.isidroid.b21.ui.home

import com.isidroid.b21.domain.model.Post

interface HomeView {
    fun onEvent(logs: List<String>)
}