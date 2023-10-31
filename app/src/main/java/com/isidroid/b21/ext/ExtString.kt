package com.isidroid.b21.ext

import com.isidroid.core.ext.capitalize

fun String.makeSingleLine(): String = this.replace("\n", "  ")

fun String.splitByLetter(): String {
    val list = replace(" ", "").replace("\n", "").split("#").filter { !it.isNullOrBlank() }.map { it.lowercase().trim() }
    var currentLetter: String? = null

    return buildString {
        for (item in list) {
            val firstLetter = item.take(1)

            if (currentLetter != firstLetter) {
                currentLetter = firstLetter
                append("\n")
            }

            append("#${item.capitalize()} ")
        }
    }
}

