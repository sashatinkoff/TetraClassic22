package com.isidroid.b21.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sessions")
data class Session(@PrimaryKey val id: String)