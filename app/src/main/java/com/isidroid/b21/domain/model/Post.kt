package com.isidroid.b21.domain.model

import java.util.Date

data class Post(
    val id: String,
    val createdAt: Date,
    val url: String,
    val html: String,
    val text: String,
    val title: String?,

    val getByUrl: String = ""
)