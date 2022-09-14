package com.isidroid.b21.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.isidroid.b21.domain.model.Record
import com.isidroid.b21.domain.model.Session

@Dao
interface SessionDao {
    @Query("SELECT * FROM telegram_post_record WHERE id = :id")
    fun find(id: String): Record?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg record: Record)
}