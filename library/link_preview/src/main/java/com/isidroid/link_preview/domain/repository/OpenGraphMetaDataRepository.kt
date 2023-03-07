package com.isidroid.link_preview.domain.repository

import com.isidroid.link_preview.domain.model.LinkSourceContent
import org.jsoup.nodes.Document

interface OpenGraphMetaDataRepository {
     fun parseFullContent(url: String): LinkSourceContent?
     fun parseHeaderContent(url: String): LinkSourceContent?
}