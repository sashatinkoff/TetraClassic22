package com.isidroid.b21.domain.repository

import com.isidroid.b21.domain.model.Post

interface LiveJournalRepository {
    suspend fun postHtml(url: String): Post
    suspend fun nextPost(postId: String, isNext: Boolean = true): Post
    suspend fun parsePost(html: String): Post
}