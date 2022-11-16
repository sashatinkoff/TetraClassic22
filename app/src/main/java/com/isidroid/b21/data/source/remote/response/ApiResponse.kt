package com.isidroid.b21.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("info") val info: InfoResponse,
    @SerializedName("results") val result: T
) {
    data class InfoResponse(
        @SerializedName("count") val count: Int,
        @SerializedName("pages") val pages: Int,
    )
}