package com.isidroid.b21.data

import android.annotation.SuppressLint
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.isidroid.b21.R
import com.isidroid.b21.utils.NotificationsChannels
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit


@HiltWorker
class InboxWorker constructor(
    appContext: Context,
    workerParams: WorkerParameters,

    ) : Worker(appContext, workerParams) {

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun doWork(): Result {
        val title = inputData.getString("title")
        val code = inputData.getInt("code", 10)

        notification(
            code = code,
            title = title.orEmpty(),
            message = "$title completed"
        )

        return Result.success()
    }

    private fun notification(code: Int, title: String, message: String) {
        val notification = NotificationCompat.Builder(applicationContext, NotificationsChannels.DEFAULT_CHANNEL)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_notification_message)
            .setColorized(true)

        NotificationManagerCompat.from(applicationContext).notify(code, notification.build())
    }

    companion object {
        fun schedule(context: Context) {
            val workManager = WorkManager.getInstance(context)


            createNetwork(workManager)
            createSimple(workManager)
        }

        private fun createNetwork(workManager: WorkManager) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .build()

            val progressData = workDataOf("title" to "network", "code" to 100)
            val builder = PeriodicWorkRequestBuilder<InboxWorker>(
                repeatInterval = 30,
                repeatIntervalTimeUnit = TimeUnit.MINUTES,
            )

            val requestBuilder = builder.setConstraints(constraints)
                .setInputData(progressData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 20, TimeUnit.SECONDS)
                .addTag("network")


            workManager.enqueueUniquePeriodicWork("network", ExistingPeriodicWorkPolicy.REPLACE, requestBuilder.build())
        }

        private fun createSimple(workManager: WorkManager) {
            val constraints = Constraints.Builder()
                .build()

            val progressData = workDataOf("title" to "simple", "code" to 200)
            val builder = PeriodicWorkRequestBuilder<InboxWorker>(
                repeatInterval = 1,
                repeatIntervalTimeUnit = TimeUnit.HOURS,
            )

            val requestBuilder = builder.setConstraints(constraints)
                .setInputData(progressData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 20, TimeUnit.SECONDS)
                .addTag("simple")


            workManager.enqueueUniquePeriodicWork("simple", ExistingPeriodicWorkPolicy.REPLACE, requestBuilder.build())
        }
    }
}