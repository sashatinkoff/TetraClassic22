package com.isidroid.core.ext

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.isidroid.core.utils.ImageHeaderParser
import timber.log.Timber
import java.io.*

fun File.copyInputStreamToFile(inputStream: InputStream?) {
    outputStream().use { fileOut -> inputStream?.copyTo(fileOut) }
}

fun File.saveString(fileContent: String) = printWriter().use { out -> out.println(fileContent) }

fun String.assetsFileContent(context: Context) = context.assets.open(this).bufferedReader().use { it.readText() }

val String.fileNameExtension get() = if (contains(".")) substring(lastIndexOf(".") + 1, length) else takeLast(3)

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

fun Uri.toBitmap(context: Context): Bitmap? {
    var stream: InputStream? = null
    val bitmap = try {

        try {
            stream = stream(context)

            val imageHeaderParser = ImageHeaderParser(stream)
            val orientation = imageHeaderParser.orientation
            val angle = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                ExifInterface.ORIENTATION_TRANSVERSE -> 270
                else -> 0
            }

            stream?.close()
            stream = stream(context)
            val bitmap = BitmapFactory.decodeStream(stream)

            if (orientation >= 0)
                bitmap.rotateFullImage(angle.toFloat())
            else
                bitmap
        } catch (t: Throwable) {
            stream?.close()
            stream = stream(context)
            BitmapFactory.decodeStream(stream)
        }

    } catch (t: Throwable) {
        Timber.e("Failed to convert uri to Bitmap: context=$context, uri=$this")
        Timber.e(t)
        null
    } finally {
        stream?.close()
    }

    return bitmap
}