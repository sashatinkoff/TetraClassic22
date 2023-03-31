package com.isidroid.b21.domain.use_case

import com.isidroid.b21.data.source.remote.api.ApiTest
import com.isidroid.b21.domain.repository.DwollaRepository
import com.isidroid.core.ext.Time
import com.isidroid.link_preview.domain.model.LinkSourceContent
import com.isidroid.link_preview.domain.repository.OpenGraphMetaDataRepository
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeUseCase @Inject constructor(
    private val dwollaRepository: DwollaRepository
) {
    fun createCustomer(name: String, lastName: String) = flow {
        val email = "${name}_$lastName@fakedomain.com"

        val customerId = dwollaRepository.createCustomer(
            name = name,
            lastName = lastName,
            email = email,
            ssn = "9876"
        )

//        val customerId = "6eb1cab5-02f1-4cc7-b397-f1da0a6c3183"
        dwollaRepository.checkStatus(customerId)

        emit(true)
    }
}