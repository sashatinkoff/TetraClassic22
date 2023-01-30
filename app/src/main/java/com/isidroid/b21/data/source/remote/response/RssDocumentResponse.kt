package com.isidroid.b21.data.source.remote.response

import com.google.gson.annotations.SerializedName
import java.util.*

data class RssDocumentResponse(
    @SerializedName("rss") val rss: RssResponse
) {
    data class RssResponse(@SerializedName("channel") val channel: ChannelResponse)
    data class ChannelResponse(@SerializedName("item") val items: List<ItemResponse>)
    data class ItemResponse(
        @SerializedName("title") val title: RssDocumentItemResponse<String>,
        @SerializedName("link") val link: RssDocumentItemResponse<String>,
        @SerializedName("guid") val guid: RssDocumentItemResponse<String>,
        @SerializedName("description") val description: RssDocumentItemResponse<String>,
        @SerializedName("pubDate") val pubDate: RssDocumentItemResponse<Date>,
    )
}

data class RssDocumentItemResponse<T>(@SerializedName("__cdata") val data: T)