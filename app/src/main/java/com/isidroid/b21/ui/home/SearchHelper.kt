package com.isidroid.b21.ui.home

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapNotNull

class SearchHelper<R>(private val listener: Listener<R>) {
    private val query = MutableStateFlow<String?>(null)
    private var isInitialized = false
    private val params = hashMapOf<String, Any>()

    private var queryText: String?
        get() = query.value
        set(value) {
            isInitialized = true
            query.value = value
        }


    val hasQuery get() = queryText != null

    fun reset() = apply { params.clear() }
    fun addParam(key: String, value: Any) = apply { params[key] = value }
    fun updateQuery(query: String?) = apply { this.queryText = query }

    @OptIn(FlowPreview::class)
    val filteredQuery = query
        .debounce(400)
        .distinctUntilChanged()
        .flowOn(Dispatchers.IO)
        .mapNotNull {
            if (isInitialized)
                listener.filterQuery(query = it, params = params)
            else
                null
        }
        .flowOn(Dispatchers.IO)

    fun interface Listener<R> {
        suspend fun filterQuery(query: String?, params: Map<String, Any>): R
    }
}