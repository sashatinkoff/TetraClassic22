package com.isidroid.b21.ext

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.isidroid.b21.ui.main.MainActivity
import com.isidroid.core.ext.getPendingIntent


fun Context.createPendingIntent(action: String? = null): PendingIntent {
    val intent = Intent(applicationContext, MainActivity::class.java)
        .apply {
            addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            addCategory(Intent.CATEGORY_LAUNCHER)

            action?.also { this.action = it }
        }

    return intent.getPendingIntent(this)
}