package com.isidroid.link_preview.domain.model

data class LinkSourceContent(
    val title: String,
    val description: String,
    val url: String,
    val image: String,
    val siteName: String,
    val type: String
) {
    override fun toString(): String {
        return "LinkSourceContent(title='${title.take(50)}', description='${description.take(50)}', url='$url', image='$image', siteName='$siteName', type='$type')"
    }
}
