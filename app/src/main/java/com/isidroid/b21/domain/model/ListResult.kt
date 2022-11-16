package com.isidroid.b21.domain.model

data class ListResult<T>(
    val total: Int,
    val list: List<T>
)