package com.isidroid.link_preview.domain.repository

import com.isidroid.link_preview.domain.model.LinkSourceContent

interface OpenGraphMetaDataRepository {
    suspend fun parse(url: String): LinkSourceContent?
}