package com.isidroid.b21.data.worker.sample.sample_worker

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.work.WorkManager

interface SampleWorkerListener {
    fun onWorkerProgress(title: String?, progress: Int, total: Int)
    fun onWorkerComplete(title: String?)
    fun onWorkerError(message: String)

    companion object {
        fun listen(context: Context, lifecycleOwner: LifecycleOwner, listener: SampleWorkerListener) {
            val workManager = WorkManager.getInstance(context)

            workManager.getWorkInfosForUniqueWorkLiveData(SampleWorker::class.java.simpleName)
                .observe(lifecycleOwner) { listOfWorkInfo ->
                    for (workInfo in listOfWorkInfo) {
                        val title = workInfo.progress.getString("title") ?: workInfo.outputData.getString("title")
                        val message = workInfo.progress.getString("message") ?: workInfo.outputData.getString("message")
                        val progress = workInfo.progress.getInt("message", -1)
                        val total = workInfo.progress.getInt("total", -1)

                        val isCompleted = (workInfo.state.isFinished)

                        when {
                            isCompleted && message != null -> listener.onWorkerError(message)
                            isCompleted -> listener.onWorkerComplete(title)
                            else -> listener.onWorkerProgress(title, progress, total)
                        }
                    }
                }
        }
    }
}