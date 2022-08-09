package com.isidroid.core.data.mapper

interface Mapper<L, N> {
    fun transformLocal(item: L): N? = null
    fun transformNetwork(item: N): L
    fun transformNetworkList(items: List<N>?): List<L>? = items?.map { transformNetwork(it) }
}