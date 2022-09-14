package com.isidroid.b21.data.repository

import com.isidroid.b21.SendMessageException
import com.isidroid.b21.data.source.local.AppDatabase
import com.isidroid.b21.data.source.remote.api.ApiTest
import com.isidroid.b21.domain.model.Post
import com.isidroid.b21.domain.model.Record
import com.isidroid.b21.domain.repository.TestRepository
import com.isidroid.b21.ext.compatTelegramHtml
import com.isidroid.b21.ext.result
import org.jsoup.Jsoup
import org.jsoup.safety.Safelist
import timber.log.Timber

private val MAX_LENGTH = 4096

class TestRepositoryImpl(
    private val apiTest: ApiTest,
    private val database: AppDatabase
) : TestRepository {
    override suspend fun sendMessage(post: Post): Boolean {
        var id = post.url
        val safeList = Safelist()
        safeList.addTags("b", "u", "s", "b", "a", "code", "pre", "br")
        val fixedMessage = Jsoup.clean(post.message, safeList).compatTelegramHtml

        return if (fixedMessage.length > MAX_LENGTH) {
            val chunked = fixedMessage.chunked(MAX_LENGTH)
            chunked.take(1).forEachIndexed { index, message ->
                val chunkId = "$id::$index"

                Timber.i("sdfsdf $index). msg=${message.length}")

                sendMessage(id = id, message = message)
            }

            true

        } else {
            sendMessage(id = id, message = fixedMessage)
        }

    }

    fun sendMessage(id: String, message: String): Boolean {
        val record = database.recordDao().find(id) ?: Record(id = id)
        val result = record.isSaved

        if (!record.isSaved) {
            val response = apiTest.sendMessage(
                botApiToken = "5530747084:AAFE_uhQr5hqZSS-GWM4yPacKdBAn0Z0f94",
                channelId = "@test_channel_291182",
                text = message
            ).execute()

            val data = response.result {
                SendMessageException(
                    response.errorBody()?.string() ?: "Some error occurred"
                )
            }
            record.isSaved = true
            database.recordDao().insert(record)

        }

        return result
    }
}