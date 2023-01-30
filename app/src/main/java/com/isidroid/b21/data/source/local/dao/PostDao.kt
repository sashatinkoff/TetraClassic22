package com.isidroid.b21.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.isidroid.b21.domain.model.Post

@Dao
interface PostDao {
    @Query("SELECT * FROM post WHERE id = :id")
    fun findById(id: String): Post?

    @Query("SELECT * FROM post WHERE url = :url")
    fun findByUrl(url: String): Post?

    @Query("SELECT * FROM post WHERE getByUrl = :url")
    fun findByGetByUrl(url: String): Post?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg post: Post)
}