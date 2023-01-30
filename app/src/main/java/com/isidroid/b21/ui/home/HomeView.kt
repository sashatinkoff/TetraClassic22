package com.isidroid.b21.ui.home

import com.isidroid.b21.domain.model.Post

interface HomeView {
    fun onContent(post: Post)
    fun onLoading(url: String)
    fun onPostFoundLocal(post: Post)
    fun appendText(text: String)
}