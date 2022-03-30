package com.isidroid.b21.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.isidroid.b21.domain.model.Session

@Dao
interface SessionDao {
    @Query("SELECT * FROM sessions WHERE id = :id")
    fun find(id: Int): Session?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg sessions: Session)
}