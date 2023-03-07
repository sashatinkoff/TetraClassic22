package com.isidroid.b21.domain.use_case

import com.isidroid.b21.data.source.remote.api.ApiTest
import com.isidroid.core.ext.Time
import com.isidroid.link_preview.domain.model.LinkSourceContent
import com.isidroid.link_preview.domain.repository.OpenGraphMetaDataRepository
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeUseCase @Inject constructor(
    private val openGraphMetaDataRepository: OpenGraphMetaDataRepository,
    private val apiTest: ApiTest
) {
    fun preview(links: Array<String>) = flow {


        emit("")
    }
}