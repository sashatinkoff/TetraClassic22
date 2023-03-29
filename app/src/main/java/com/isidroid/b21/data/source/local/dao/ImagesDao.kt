package com.isidroid.b21.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.isidroid.b21.domain.model.ImageInfo

@Dao
interface ImagesDao {
    @Query("DELETE FROM imageinfo")
    fun deleteAll()

    @Query("SELECT * FROM imageinfo WHERE url = :url")
    fun find(url: String): ImageInfo?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(list: List<ImageInfo>)
}