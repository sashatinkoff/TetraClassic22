package com.isidroid.link_preview.data.repository

import com.fresh.materiallinkpreview.models.OpenGraphMetaData
import com.fresh.materiallinkpreview.models.OpenGraphNamespace
import com.isidroid.link_preview.domain.repository.HtmlHeadParserRepository
import com.isidroid.link_preview.domain.repository.OpenGraphMetaDataRepository
import com.isidroid.link_preview.ext.isImage
import org.jsoup.Jsoup
import java.util.regex.Pattern

class OpenGraphMetaDataRepositoryImpl(private val htmlHeadParserRepository: HtmlHeadParserRepository) : OpenGraphMetaDataRepository {

    override suspend fun startFetchingMetadata(link: String): OpenGraphMetaData {
        if (link.isImage)
            return OpenGraphMetaData("", link, link, "website")

        val _pageNamespaces = mutableListOf<OpenGraphNamespace>()

        val completeHeadContents = htmlHeadParserRepository.getHtmlHeader(link)
        val document = Jsoup.parse(completeHeadContents)

        var hasOpenGraphSpecification = false

        // try and identify any namespace information if it exists
        if (document.head().hasAttr("prefix")) {
            val prefixElements = document.head().getElementsByAttribute("prefix").html()
            val pattern = Pattern.compile("((\\w+):\\s+(https://ogp.me/ns(/\\w+)*#))\\s*")
            val matcher = pattern.matcher(prefixElements)

            while (matcher.find()) {
                val prefix = matcher.group(2)
                val documentUri = matcher.group(3)

                if (!prefix.isNullOrEmpty() && !documentUri.isNullOrEmpty()) {
                    _pageNamespaces.add(OpenGraphNamespace(prefix, documentUri))
                }

                hasOpenGraphSpecification = !prefix.isNullOrEmpty() && prefix.equals("og")
            }
        }

        // if no namespace information exists, add some defaults
        if (!hasOpenGraphSpecification) {
            _pageNamespaces.add(OpenGraphNamespace("og", "https://ogp.me/ns#"))
            _pageNamespaces.add(OpenGraphNamespace("twitter", ""))
        }

        val metaElements = document.head().getElementsByTag("meta")
        val titleElement = document.head().getElementsByTag("title")
        val openGraphMetaData = OpenGraphMetaData()

        var twitterTitle : String? = null
        var twitterDescription : String? = null
        var twitterImageUrl : String? = null

        // go through every meta element that we have collected, and try to identify any open graph tags
        // once we have identified them, sort them appropriately into the data structure
        for (metaElement in metaElements) {
            for (namespace in _pageNamespaces) {
                var target = ""

                if (metaElement.hasAttr("property")) {
                    target = "property"
                } else if (metaElement.hasAttr("name")) {
                    target = "name"
                }

                if (target.isNotEmpty()
                    && metaElement.attr(target).startsWith(namespace.prefix + ":")
                ) {
                    when (metaElement.attr(target)) {
                        "og:title" -> openGraphMetaData.title = metaElement.attr("content")
                        "og:type" -> openGraphMetaData.type = metaElement.attr("content")
                        "og:image" -> openGraphMetaData.imageUrl = metaElement.attr("content")
                        "og:url" -> openGraphMetaData.url = metaElement.attr("content")
                        "og:description" -> openGraphMetaData.description =
                            metaElement.attr("content")
                        "twitter:title" -> twitterTitle = metaElement.attr("content")
                        "twitter:description" -> twitterDescription = metaElement.attr("content")
                        "twitter:image" -> twitterImageUrl = metaElement.attr("content")
                    }

                    break
                }
            }
        }

        if (openGraphMetaData.title.isEmpty() && titleElement.count() == 1) {
            openGraphMetaData.title = titleElement[0].childNode(0).toString()
        }

        if (openGraphMetaData.url.isEmpty()) {
            openGraphMetaData.url = link
        }

        // if the title, description or imageUrl have not been found, assign via twitter tags, that we may have collected
        if(openGraphMetaData.title.isEmpty() && !twitterTitle.isNullOrEmpty()) {
            openGraphMetaData.title = twitterTitle
        }

        if(openGraphMetaData.description.isNullOrEmpty() && !twitterDescription.isNullOrEmpty()) {
            openGraphMetaData.description = twitterDescription
        }

        if(openGraphMetaData.imageUrl.isEmpty() && !twitterImageUrl.isNullOrEmpty()) {
            openGraphMetaData.imageUrl = twitterImageUrl
        }

        return openGraphMetaData
    }
}