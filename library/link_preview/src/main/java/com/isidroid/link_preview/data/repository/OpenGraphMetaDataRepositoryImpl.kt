package com.isidroid.link_preview.data.repository

import com.isidroid.link_preview.data.source.network.ApiLinkPreviewParser
import com.isidroid.link_preview.domain.model.LinkSourceContent
import com.isidroid.link_preview.domain.repository.OpenGraphMetaDataRepository
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import timber.log.Timber

private const val OG_TITLE: String = "og:title"
private const val OG_DESCRIPTION: String = "og:description"
private const val OG_TYPE: String = "og:type"
private const val OG_IMAGE: String = "og:image"
private const val OG_URL: String = "og:url"
private const val OG_SITE_NAME: String = "og:site_name"

class OpenGraphMetaDataRepositoryImpl(private val api: ApiLinkPreviewParser) : OpenGraphMetaDataRepository {

    override fun parseFullContent(url: String): LinkSourceContent? {
        val jsoup = Jsoup.connect(url)
        val response = jsoup
            .ignoreContentType(true)
            .userAgent("Mozilla")
            .referrer("http://www.google.com")
            .timeout(12000)
            .followRedirects(false)
            .execute()
        val doc = response.parse()
        return organizeFetchedData(doc, url)
    }

    override fun parseHeaderContent(url: String): LinkSourceContent? {
        return try {
            val responseBody = api.get(url).execute().body()!!
            val data = StringBuilder()

            responseBody.byteStream().use { inputStream ->
                val buffer = ByteArray(100)
                var progressBytes = 0L
                var bytes = inputStream.read(buffer)
                while (bytes >= 0) {
                    progressBytes += bytes
                    bytes = inputStream.read(buffer)
                    val string = String(buffer, Charsets.UTF_8)
                    data.append(string)

                    if (string.contains("</head>")) {
                        data.append("<body></body></html>")
                        break
                    }
                }
            }

            val document = Jsoup.parse(data.toString())
            organizeFetchedData(document, url)
        } catch (t: Throwable) {
            null
        }
    }


    private fun organizeFetchedData(doc: Document, url: String): LinkSourceContent {
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
            url = data[OG_URL] ?: url,
            siteName = data[OG_SITE_NAME].orEmpty(),
            type = data[OG_TYPE].orEmpty(),
        )
    }

}