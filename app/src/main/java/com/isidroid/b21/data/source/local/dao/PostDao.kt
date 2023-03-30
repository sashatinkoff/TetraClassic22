package com.isidroid.b21.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.isidroid.b21.domain.model.Post
import java.util.Date

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

    @Query("SELECT * FROM post ORDER BY createdAt")
    fun all(): List<Post>

    @Query("SELECT * FROM post WHERE createdAt BETWEEN :start AND :end ORDER BY createdAt")
    fun filterByDate(start: Date, end: Date): List<Post>

    @Query("SELECT COUNT() FROM post WHERE source = :source")
    fun countBySource(source: String): Int

    @Query("SELECT COUNT() FROM post WHERE source = :source AND isDownloaded = '1'")
    fun countBySourceDownloaded(source: String): Int

    @Query("DELETE FROM post WHERE source = 'liveInternet'")
    fun deleteLiveInternet()

    @Query("SELECT * FROM post ORDER BY createdAt DESC LIMIT 1")
    fun findLast(): Post?
}