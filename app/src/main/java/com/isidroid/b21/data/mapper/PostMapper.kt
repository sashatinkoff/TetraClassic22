package com.isidroid.b21.data.mapper

import com.isidroid.b21.data.source.remote.response.RssDocumentResponse
import com.isidroid.b21.domain.model.Post
import com.isidroid.core.ext.md5
import org.jsoup.Jsoup
import timber.log.Timber

object PostMapper {
    fun transformNetwork(response: RssDocumentResponse.ItemResponse): Post {
        val document = Jsoup.parse(response.description.data)
        val id = "li_${response.guid.data.md5()}"


        return Post(
            id = id,
            createdAt = response.pubDate.data,
            url = response.guid.data,
            html = document.html(),
            text = document.text(),
            title = response.title.data,
            getByUrl = response.guid.data,
            isDownloaded = true,
            source = "liveInternet"
        )
    }
}