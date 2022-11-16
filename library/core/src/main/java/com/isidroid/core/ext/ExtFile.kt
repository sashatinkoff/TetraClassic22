package com.isidroid.core.ext

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStream

fun File.copyInputStreamToFile(inputStream: InputStream?) {
    outputStream().use { fileOut -> inputStream?.copyTo(fileOut) }
}

fun File.saveString(fileContent: String) = printWriter().use { out -> out.println(fileContent) }
fun File.copyToPublicFolder(context: Context, targetDisplayName: String, destUri: Uri): Boolean {
    val documentFile = DocumentFile.fromTreeUri(context, destUri) ?: return false
    val createFile = documentFile.createFile("text/plain", targetDisplayName)
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