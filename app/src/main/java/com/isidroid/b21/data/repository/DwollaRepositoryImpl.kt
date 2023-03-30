package com.isidroid.b21.data.repository

import com.isidroid.b21.data.source.remote.api.ApiDwolla
import com.isidroid.b21.data.source.remote.request.DwollaCreateCustomerReq
import com.isidroid.b21.domain.repository.DwollaRepository

class DwollaRepositoryImpl(private val apiDwolla: ApiDwolla) : DwollaRepository {
    override fun createCustomer(name: String, lastName: String, email: String) {
        val request = DwollaCreateCustomerReq(
            firstName = name,
            lastName = lastName,
            email = email
        )
        apiDwolla.createCustomer(request).execute()
    }
}