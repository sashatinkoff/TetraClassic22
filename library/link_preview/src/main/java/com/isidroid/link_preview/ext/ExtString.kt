package com.isidroid.link_preview.ext

import java.net.URL

val String.isImage: Boolean
    get() {
        val possible = arrayOf(".gif", ".jpg", ".jpeg", ".png", ".bmp", ".tiff")

        val index = this.lastIndexOf(".")

        if (index == -1)
            return false

        val extension: String = substring(index)
        return possible.contains(extension)
    }