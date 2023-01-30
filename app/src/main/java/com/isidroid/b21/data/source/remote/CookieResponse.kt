package com.isidroid.b21.data.source.remote

import com.google.gson.annotations.SerializedName

data class CookieResponse(
    @SerializedName("domain") val domain: String,
    @SerializedName("name") val name: String,
    @SerializedName("value") val value: String,
    @SerializedName("path") val path: String,
) {
    val cookie get() = "$name=$value; path=$path; domain=$domain"
}