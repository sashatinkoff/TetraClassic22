package com.isidroid.b21.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi


class NotificationsChannels(private val context: Context) {
    private val DEFAULT_SOUND = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) create()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun create() {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(defaultChannel())
        manager.createNotificationChannel(download())
        manager.createNotificationChannel(inboxNotifications())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun defaultChannel(): NotificationChannel {
        val name = "Default channel"
        val description = "Couple of words about purposes of this channel"

        return NotificationChannel(
            DEFAULT_CHANNEL,
            name,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            this.description = description
            enableLights(true)
            lightColor = Color.BLUE
            enableVibration(true)
            setShowBadge(true)
            setSound(DEFAULT_SOUND, null)
            vibrationPattern = longArrayOf(100L, 200L, 300L, 400L, 500L, 400L, 300L, 200L, 400L)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun download(): NotificationChannel {
        val name = "Download channel"
        val description = ""

        return NotificationChannel(
            DOWNLOAD,
            name,
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            this.description = description
            enableLights(false)
            lightColor = Color.BLUE
            enableVibration(false)
            setShowBadge(false)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun inboxNotifications(): NotificationChannel {
        val name = "Inbox notifications"
        val description = ""

        return NotificationChannel(
            INBOX,
            name,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            this.description = description
            enableLights(false)
            lightColor = Color.BLUE
            enableVibration(true)
            setShowBadge(true)
        }
    }

    companion object {
        const val DEFAULT_CHANNEL = "Default channel"
        const val DOWNLOAD = "Download channel"
        const val INBOX = "Inbox notifications"
    }
}