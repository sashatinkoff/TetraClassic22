package com.isidroid.b21.data.mapper

interface Mapper<L, N> {
    fun transformLocal(item: N): L
    fun transformNetwork(item: L): N
}