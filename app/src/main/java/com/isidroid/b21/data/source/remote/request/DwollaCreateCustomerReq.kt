package com.isidroid.b21.data.source.remote.request

import com.google.gson.annotations.SerializedName

data class DwollaCreateCustomerReq(
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("email") val email: String,
    @SerializedName("type") val type: String = "personal",
    @SerializedName("address1") val address1: String = "99-99 33rd St",
    @SerializedName("city") val city: String = "Some City",
    @SerializedName("state") val state: String = "NY",
    @SerializedName("postalCode") val postalCode: String = "11101",
    @SerializedName("dateOfBirth") val dateOfBirth: String = "1970-01-01",
    @SerializedName("ssn") val ssn: String = "12345",
)