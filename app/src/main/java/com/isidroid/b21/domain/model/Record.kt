package com.isidroid.b21.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "telegram_post_record")
data class Record(@PrimaryKey val id: String, var isSaved: Boolean = false)