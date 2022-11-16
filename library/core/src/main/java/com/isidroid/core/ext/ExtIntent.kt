package com.isidroid.core.ext

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build

fun Intent.getPendingIntent(context: Context): PendingIntent {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        PendingIntent.getActivity(context, 0, this, PendingIntent.FLAG_IMMUTABLE)
    } else {
        PendingIntent.getActivity(context, 0, this, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}