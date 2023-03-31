package com.isidroid.b21.data.source.remote.api

import com.google.gson.annotations.SerializedName
import retrofit2.http.POST

interface ApiPlaid {

    @POST("link/token/create")
    fun createLinkToken()
}

data class CreateLinkTokenRequest(
    @SerializedName("client_id") val clientId: String,
    @SerializedName("secret") val secret: String,
    @SerializedName("user") val user: UserReq,
    @SerializedName("client_name") val clientName: String,
    @SerializedName("products") val products: List<String>,
    @SerializedName("language") val language: String = "en",
    @SerializedName("country_codes") val countryCodes: List<String> = listOf("US"),

) {
    data class UserReq(@SerializedName("client_user_id") val id: String)
}
