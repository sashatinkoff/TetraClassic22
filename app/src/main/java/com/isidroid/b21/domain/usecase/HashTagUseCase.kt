package com.isidroid.b21.domain.usecase

import com.google.gson.Gson
import com.isidroid.core.ext.date
import com.isidroid.core.ext.fromJson
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HashTagUseCase @Inject constructor(
    private val gson: Gson
) {
    fun collectTags(content: String?) = flow {
        val response = gson.fromJson<Response>(content.orEmpty())
        val result = LinkedHashMap<String, Int>()

        val hashTags: List<String> = response.messages.map { m -> m.textEntities.filter { it.type == "hashtag" }.map { it.text } }.flatten().map { it.lowercase() }
        val unique = hashTags.distinct().sorted()

        for (tag in unique)
            result[tag] = hashTags.filter { it == tag }.size

        emit(result)
    }
}