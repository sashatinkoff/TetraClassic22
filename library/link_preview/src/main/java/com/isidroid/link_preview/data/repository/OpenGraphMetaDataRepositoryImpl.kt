package com.isidroid.link_preview.data.repository

import com.isidroid.link_preview.domain.model.LinkSourceContent
import com.isidroid.link_preview.domain.repository.OpenGraphMetaDataRepository
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

private const val OG_TITLE: String = "og:title"
private const val OG_DESCRIPTION: String = "og:description"
private const val OG_TYPE: String = "og:type"
private const val OG_IMAGE: String = "og:image"
private const val OG_URL: String = "og:url"
private const val OG_SITE_NAME: String = "og:site_name"

class OpenGraphMetaDataRepositoryImpl : OpenGraphMetaDataRepository {
    override suspend fun parse(url: String): LinkSourceContent? {
        val response = Jsoup.connect(url)
            .ignoreContentType(true)
            .userAgent("Mozilla")
            .referrer("http://www.google.com")
            .timeout(12000)
            .followRedirects(true)
            .execute()
        val doc = response.parse()
        return organizeFetchedData(doc)
    }

    private fun organizeFetchedData(doc: Document): LinkSourceContent {
        val tags = doc.select("meta[property^=og:]")
        val data = hashMapOf<String, String>()

        tags.forEach { tag ->
            val property = tag.attr("property")
            val content = tag.attr("content")

            data[property] = content
        }

        return LinkSourceContent(
            title = data[OG_TITLE].orEmpty(),
            image = data[OG_IMAGE].orEmpty(),
            description = data[OG_DESCRIPTION].orEmpty(),
            url = data[OG_URL].orEmpty(),
            siteName = data[OG_SITE_NAME].orEmpty(),
            type = data[OG_TYPE].orEmpty(),
        )
    }

}