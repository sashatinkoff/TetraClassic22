package com.isidroid.b21.domain.usecase

import com.google.gson.annotations.SerializedName
import java.util.*

data class Response(@SerializedName("messages") val messages: List<MessageResponse>) {
    data class MessageResponse(
        @SerializedName("id") val id: String,
        @SerializedName("date") val date: Date,
        @SerializedName("date_unixtime") val timestamp: Long,
        @SerializedName("text_entities") val textEntities: List<TextEntity>,
        @SerializedName("photo") val photo: String?
    )

    data class TextEntity(
        @SerializedName("type") val type: String,
        @SerializedName("text") val text: String,
    )
}