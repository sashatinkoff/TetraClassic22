package com.isidroid.core.ext

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import timber.log.Timber
import java.io.*

fun DocumentFile.readText(context: Context): String? {
    val outputStream = tryCatch { context.contentResolver.openInputStream(uri) } ?: return null
    var content: String? = null
    try {
        val reader = BufferedReader(outputStream.reader())

        reader.use { reader -> content = reader.readText() }
    } catch (_: Throwable) {
    } finally {
        outputStream.close()
    }

    return content
}

fun DocumentFile.copy(context: Context, file: File): Boolean {
    val inputStream = tryCatch { context.contentResolver.openInputStream(uri) } ?: return false
    var result = false
    try {
        file.outputStream().use { inputStream.copyTo(it) }
        result = true
    } catch (_: Throwable) {
    } finally {
        inputStream.close()
    }
    return result
}

fun File.copyToPublicFolder(
    context: Context,
    targetDisplayName: String,
    documentFile: DocumentFile,
    mimeType: String = "image/*"
): Boolean {
    if (!exists()) return false

    val createFile = documentFile.createFile(mimeType, targetDisplayName) ?: return false
    val fos = context.contentResolver.openOutputStream(createFile.uri) ?: return false
    val inputStream = FileInputStream(this)

    var count = 0
    try {
        while (count != -1) {
            val bytes: ByteArray = ByteArray(2048)
            count = inputStream.read(bytes)
            if (count == -1) {
                continue
            }
            fos.write(bytes, 0, count)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        inputStream.close()
        fos.close()
    }

    return true
}

fun DocumentFile?.toFile(context: Context, file: File): Boolean {
    this ?: return false
    file.parentFile?.mkdirs()
    file.createNewFile()

    val inputStream = file.outputStream()
    val fos = context.contentResolver.openInputStream(uri) ?: return false

    try {
        inputStream.write(fos.readBytes())
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        fos.close()
    }

    return true
}

fun File.copyToPublicFolder(
    context: Context,
    targetDisplayName: String,
    destUri: Uri,
    mimeType: String = "text/plain"
): Boolean {
    val documentFile = DocumentFile.fromTreeUri(context, destUri) ?: return false
    val createFile = documentFile.createFile(mimeType, targetDisplayName)
    val fos = context.contentResolver.openOutputStream(createFile!!.uri) ?: return false

    try {
        fos.write(readBytes())
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        fos.close()
    }

    return true
}

fun Uri.stream(context: Context): InputStream? {
    return tryCatch { context.contentResolver.openInputStream(this) }
}
