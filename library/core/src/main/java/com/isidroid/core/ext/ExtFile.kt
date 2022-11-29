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
