package com.isidroid.b21.data.repository

import androidx.core.net.toUri
import com.isidroid.b21.data.source.remote.api.ApiDwolla
import com.isidroid.b21.data.source.remote.request.DwollaCreateCustomerReq
import com.isidroid.b21.domain.repository.DwollaRepository
import timber.log.Timber

class DwollaRepositoryImpl(private val apiDwolla: ApiDwolla) : DwollaRepository {
    override fun createCustomer(name: String, lastName: String, email: String, ssn: String): String {
        val request = DwollaCreateCustomerReq(
            firstName = name,
            lastName = lastName,
            email = email,
            ssn = ssn
        )
        val response = apiDwolla.createCustomer(request).execute()
        return response.headers().firstOrNull { it.first == "location" }?.second?.toUri()?.lastPathSegment ?: throw Exception("Customer Id not found")
    }

    override fun checkStatus(customerId: String) {
        val response = apiDwolla.checkStatus(customerId).execute().body() ?: throw Exception("Customer not found")

        Timber.i("$response")
    }
}