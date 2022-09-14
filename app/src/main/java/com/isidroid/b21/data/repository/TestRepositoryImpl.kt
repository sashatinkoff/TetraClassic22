package com.isidroid.b21.data.repository

import com.isidroid.b21.SendMessageException
import com.isidroid.b21.data.source.local.AppDatabase
import com.isidroid.b21.data.source.remote.api.ApiTest
import com.isidroid.b21.domain.model.Post
import com.isidroid.b21.domain.model.Record
import com.isidroid.b21.domain.repository.TestRepository
import com.isidroid.b21.ext.compatTelegramHtml
import kotlinx.coroutines.delay
import org.jsoup.Jsoup
import org.jsoup.safety.Safelist

class TestRepositoryImpl(
    private val apiTest: ApiTest,
    private val database: AppDatabase
) : TestRepository {
    override suspend fun sendMessage(post: Post): Boolean {
        val record = database.recordDao().find(post.url) ?: Record(id = post.url)
        val result = record.isSaved

        if (!record.isSaved) {
            val safeList = Safelist()
            safeList.addTags("b", "i", "u", "s", "span", "b", "a", "code", "pre", "br")
            val fixedMessage = Jsoup.clean(post.message, safeList).compatTelegramHtml

            val response = apiTest.sendMessage(
                botApiToken = "5530747084:AAFE_uhQr5hqZSS-GWM4yPacKdBAn0Z0f94",
                channelId = "@test_channel_291182",
                text = fixedMessage
            ).execute()

            if (response.isSuccessful) {
                record.isSaved = true
                database.recordDao().insert(record)
            } else {
                val body = response.body()

                if (body?.errorCode == 429 && body.parameters?.retryAfter != null) {
                    val delay = body.parameters.retryAfter
                    delay(delay.toLong())
                    sendMessage(post)
                } else {
                    throw SendMessageException(body?.description ?: "Some error occurred")
                }
            }
        }

        return result
    }
}