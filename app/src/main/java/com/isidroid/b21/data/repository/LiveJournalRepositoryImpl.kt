package com.isidroid.b21.data.repository

import androidx.core.net.toUri
import com.isidroid.b21.data.source.remote.api.ApiLiveJournal
import com.isidroid.b21.data.source.settings.Settings
import com.isidroid.b21.domain.model.Post
import com.isidroid.b21.domain.repository.LiveJournalRepository
import com.isidroid.b21.ext.result
import org.jsoup.Jsoup
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class LiveJournalRepositoryImpl(private val api: ApiLiveJournal) : LiveJournalRepository {

    override suspend fun postHtml(url: String): Post {
        val response = api.get(url).execute()
        val html = response.result { Exception("Failed to get $url") }.string()

        return parsePost(html).copy(getByUrl = url)
    }

    override suspend fun nextPost(postId: String, isNext: Boolean): Post {
        val url = String.format(
            "https://www.livejournal.com/go.bml?journal=fixin&amp;itemid=%s&amp;dir=%s",
            postId,
            if (isNext) "next" else "prev"
        )

        return postHtml(url)
    }

    override suspend fun parsePost(html: String): Post {
        val dateCreationFormat = SimpleDateFormat("MMMM dd yyyy, HH:mm", Locale.getDefault())

        val document = Jsoup.parse(html)
        val timeDocument = document.body().getElementsByTag("time")
        val dateString = timeDocument.text()
        val date = dateCreationFormat.parse(dateString.orEmpty())
        val title = document.getElementsByTag("h1").firstOrNull()?.text()
        val content = document.getElementsByClass("aentry-post__text aentry-post__text--view")
        val contentHtml = content.html()
        val contentText = content.text()
        val url = document.getElementsByTag("meta").firstOrNull { it.attr("property") == "og:url" }?.attr("content")!!
        val id = url.toUri().lastPathSegment?.let { "\\D".toRegex().replace(it, "") }.orEmpty()

        val next = document.getElementsByClass("b-controls-prev")

        return Post(
            id = id,
            createdAt = date!!,
            title = title,
            url = url,
            html = contentHtml,
            text = contentText
        )
    }
}