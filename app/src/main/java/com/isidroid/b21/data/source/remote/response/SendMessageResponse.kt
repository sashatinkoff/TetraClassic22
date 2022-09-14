package com.isidroid.b21.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class SendMessageResponse(
    @SerializedName("ok") val isSuccess: Boolean,
    @SerializedName("error_code") val errorCode: Int?,
    @SerializedName("description") val description: String?,
    @SerializedName("parameters") val parameters: ParametersResponse?
) {
    data class ParametersResponse(
        @SerializedName("retry_after") val retryAfter: Int?
    )
}