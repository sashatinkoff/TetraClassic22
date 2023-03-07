package com.isidroid.link_preview.domain.repository

interface HtmlHeadParserRepository {
    suspend fun getHtmlHeader(link: String): String
}