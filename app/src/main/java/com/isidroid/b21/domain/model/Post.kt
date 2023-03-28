package com.isidroid.b21.domain.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(indices = [Index(value = ["getByUrl", "url"])])
data class Post(
    @PrimaryKey val id: String,
    val createdAt: Date? = null,
    val url: String,
    val html: String? = null,
    val text: String? = null,
    val title: String? = null,

    val getByUrl: String = "",
    val isDownloaded: Boolean = false,
    val source: String
) {
    val isLiveJournal: Boolean get() = source == "LiveJournal"
}