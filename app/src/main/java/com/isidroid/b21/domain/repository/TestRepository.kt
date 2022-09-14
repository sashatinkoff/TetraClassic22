package com.isidroid.b21.domain.repository

import com.isidroid.b21.domain.model.Post

interface TestRepository {
    suspend fun sendMessage(post: Post): Boolean
}