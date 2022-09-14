package com.isidroid.b21.ext

import android.content.Context

val String.compatTelegramHtml: String
    get() {
        var result = replace("<br>", "\n")
            .replace("<br />", "\n")


        val regex = "<img.+src=[\"'](.+?)[\"'].+?>".toRegex()
        val matches = regex.findAll(result)
        matches.forEach { matchResult ->
            result = result.replace(matchResult.value, matchResult.groupValues.last())
        }

        return result
    }


fun String.assetsFileContent(context: Context) = context.assets.open(this).bufferedReader().use { it.readText() }
