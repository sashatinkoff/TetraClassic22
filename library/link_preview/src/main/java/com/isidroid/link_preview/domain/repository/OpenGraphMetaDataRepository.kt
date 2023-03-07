package com.isidroid.link_preview.domain.repository

import com.fresh.materiallinkpreview.models.OpenGraphMetaData

interface OpenGraphMetaDataRepository {
    suspend fun startFetchingMetadata(link: String): OpenGraphMetaData
}