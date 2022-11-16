package com.isidroid.b21.ext

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.isidroid.b21.BuildConfig
import java.io.File

fun Context.isWorkerRunning(workName: String): Boolean {
    val workManager = WorkManager.getInstance(this)
    val list = workManager
        .getWorkInfosForUniqueWork(workName)
        .get()

    return list.any { it.state == WorkInfo.State.RUNNING }
}

fun File.getUriCompat(context: Context): Uri = FileProvider.getUriForFile(context, "${BuildConfig.APPLICATION_ID}.fileprovider", this)

val Context.tmpFileUri: Uri
    get() {
        val tmpFile = File.createTempFile("tmp_image_file", ".png", cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }

        return tmpFile.getUriCompat(this)
    }