package com.isidroid.b21.data.source.remote.response

import com.google.gson.annotations.SerializedName
import java.util.Date

data class DwollaCustomerStatusResponse(
    @SerializedName("id") val id: String,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("email") val email: String,
    @SerializedName("type") val type: String,
    @SerializedName("status") val status: String,
    @SerializedName("created") val created: Date,
    @SerializedName("address1") val address1: String,
    @SerializedName("address2") val address2: String,
    @SerializedName("city") val city: String,
    @SerializedName("state") val state: String,
    @SerializedName("postalCode") val postalCode: String,
    @SerializedName("phone") val phone: String,
)
