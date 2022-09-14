package com.isidroid.b21.ui.home

import com.isidroid.b21.domain.model.Post

interface HomeView {
    fun onProgress(currentFile: Int, filesCount: Int, currentPost: Int, postsInFileCount: Int, post: Post)
}