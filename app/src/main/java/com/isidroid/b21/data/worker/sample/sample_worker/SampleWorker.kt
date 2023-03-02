package com.isidroid.b21.data.worker.sample.sample_worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.isidroid.b21.ext.isWorkerRunning
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@HiltWorker
class SampleWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        return Result.success()
    }

    companion object {
        fun create(context: Context) {
            if (context.isWorkerRunning(SampleWorker::class.java.simpleName)) return

            val workManager = WorkManager.getInstance(context)
            val constraints = Constraints.Builder().build()

            val builder = OneTimeWorkRequestBuilder<SampleWorker>()
                .keepResultsForAtLeast(1, TimeUnit.MINUTES)

            val data = Data.Builder()
                .build()

            val requestBuilder = builder
                .setConstraints(constraints).setInputData(data)

            workManager.beginUniqueWork(SampleWorker::class.java.simpleName, ExistingWorkPolicy.REPLACE, requestBuilder.build()).enqueue()
        }
    }
}