package com.isidroid.b21.domain.repository

interface DwollaRepository {
    fun createCustomer(name: String, lastName: String, email: String)
}