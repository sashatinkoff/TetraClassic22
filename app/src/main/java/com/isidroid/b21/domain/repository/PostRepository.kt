package com.isidroid.b21.domain.repository

import com.isidroid.b21.domain.model.Post

interface PostRepository {
    fun findAll(): List<Post>
}