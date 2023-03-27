package com.isidroid.b21.data.repository

import com.isidroid.b21.data.source.local.dao.PostDao
import com.isidroid.b21.domain.model.Post
import com.isidroid.b21.domain.repository.PostRepository

class PostRepositoryImpl(private val postDao: PostDao) : PostRepository {
    override fun findAll(): List<Post> {
        return return postDao.all()
    }
}