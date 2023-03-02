package com.isidroid.b21.domain.usecase

import com.google.gson.annotations.SerializedName
import java.util.*

data class Response(@SerializedName("messages") val messages: List<MessageResponse>) {
    data class MessageResponse(
        @SerializedName("id") val id: Int,
        @SerializedName("date_unixtime") val timestamp: Long,
        @SerializedName("text_entities") val textEntities: List<TextEntity>,
        @SerializedName("photo") val photo: String?,
        @SerializedName("photo_list") var photoList: MutableSet<String>?
    ) {
        val date = Date(timestamp * 1000)
    }

    data class TextEntity(
        @SerializedName("type") val type: String,
        @SerializedName("text") val text: String,
    )
}

