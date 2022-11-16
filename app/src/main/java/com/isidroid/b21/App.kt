package com.isidroid.b21

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import androidx.work.Configuration
import com.isidroid.b21.utils.NotificationsChannels
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), Configuration.Provider  {
    @Inject lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()

        instance = this
        NotificationsChannels(this)
//        InboxWorker.schedule(this)
        Timber.plant(Timber.DebugTree())
    }

    override fun getWorkManagerConfiguration() = Configuration.Builder()
        .setWorkerFactory(workerFactory)
        .build()

    companion object {
        lateinit var instance: App
    }
}