package com.isidroid.b21.domain.use_case

import com.isidroid.link_preview.domain.repository.OpenGraphMetaDataRepository
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeUseCase @Inject constructor(
    private val openGraphMetaDataRepository: OpenGraphMetaDataRepository
) {
    fun preview(link: Array<String>) = flow {
        val result = link.map { openGraphMetaDataRepository.startFetchingMetadata(it) }
        emit(result)
    }
}