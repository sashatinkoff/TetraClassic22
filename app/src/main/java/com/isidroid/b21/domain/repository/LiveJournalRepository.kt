package com.isidroid.b21.domain.repository

import android.net.Uri
import com.isidroid.b21.domain.model.Post

interface LiveJournalRepository {
    suspend fun postHtml(url: String, attempt: Int = 1): Post
    suspend fun nextPostUrl(postId: String, isNext: Boolean = true): String
    suspend fun parsePost(html: String, getByUrl: String): Post

    suspend fun findPostById(id: String): Post?
    suspend fun findPostByUrl(url: String): Post?

    suspend fun loadLiveInternet()
    suspend fun saveToJson(uri: Uri)
}