package com.isidroid.b21.data.mapper

import com.isidroid.b21.domain.model.Post
import com.isidroid.b21.domain.model.RssDocumentResponse

object PostMapper {
    fun transformNetwork(response: RssDocumentResponse.ItemResponse): Post {
        return Post(
            url = response.guid.data,
            createdAt = response.pubDate.data,
            text = response.description.data,
            comments = null
        )
    }
}