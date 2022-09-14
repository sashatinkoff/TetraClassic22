package com.isidroid.b21.domain.model

import com.isidroid.b21.ext.formatDateTime
import java.util.*

data class Post(
    val url: String,
    val createdAt: Date,
    val text: String,
    val comments: List<Comment>?
) {
    val message: String get() = "${createdAt.formatDateTime}<br /><br />${text}<br /><br />$url"
}