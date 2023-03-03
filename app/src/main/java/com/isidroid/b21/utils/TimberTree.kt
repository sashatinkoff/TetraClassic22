package com.isidroid.b21.utils

import com.isidroid.core.ext.string
import timber.log.Timber
import java.util.*

class TimberTree : Timber.DebugTree() {
    private val logsCollector = mutableListOf<String>()

    private fun skipLogTag(tag: String?): Boolean {
        var skip = false
        arrayOf(
            "Billing",
            "AdCache",
            "AdManager",
            "activity_lifecycle",
            "fragment_lifecycle"
        ).forEach {
            if (tag?.contains(it, true) == true) {
                skip = true
                return@forEach
            }
        }

        return skip
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        super.log(priority, tag, message, t)

        if (!skipLogTag(tag))
            logsCollector.add("${Date().string} $message")
    }

}