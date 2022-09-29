package com.isidroid.core.ext

import androidx.core.net.toUri
import java.io.File
import java.security.MessageDigest
import java.text.DecimalFormat
import kotlin.math.ln
import kotlin.math.log10
import kotlin.math.pow

fun Long.withSuffix(): String {
    if (this < 1000) return "" + this
    val exp = (ln(this.toDouble()) / ln(1000.0)).toInt()
    return String.format(
        "%.1f%c",
        this / 1000.0.pow(exp.toDouble()),
        "kMGTPE"[exp - 1]
    )
}

fun Int.withSuffix() = this.toLong().withSuffix()

fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    val digested = md.digest(toByteArray())
    return digested.joinToString("") { String.format("%02x", it) }
}

fun String.clearUrl() = with(toUri()) {
    toString()
        .replace(query.orEmpty(), "")
        .replace("^/+".toRegex(), "")
}

fun String.toFileName() = take(20)
    .let { "\\s".toRegex().replace(it, "_") }
    .let { "[^a-zA-Z0-9 ]".toRegex().replace(it, "_") }
    .let { "_{2,}".toRegex().replace(it, "") }
    .lowercase()

fun Long.toFileSize(): String {
    if (this <= 0) return "0";
    val size = toDouble()

    val units = listOf("B", "KB", "MB", "GB", "TB")
    val digitGroups = log10(size) / log10(1024.0)

    return DecimalFormat("#,##0.#").format(size / 1024.0.pow(digitGroups)) + " " + units[digitGroups.toInt()]
}

