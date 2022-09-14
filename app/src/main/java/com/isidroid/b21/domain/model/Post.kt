package com.isidroid.b21.domain.model

import com.isidroid.b21.ext.formatDateTime
import java.text.SimpleDateFormat
import java.util.*

data class Post(
    val url: String,
    val createdAt: Date,
    val text: String,
    val comments: List<Comment>?
) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("ru", "RU"))

    val message: String get() = "${dateFormat.format(createdAt)}<br /><br />${text}<br /><br />$url"
}