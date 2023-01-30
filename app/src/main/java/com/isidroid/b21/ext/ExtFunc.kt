package com.isidroid.b21.ext

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.navigation.NavController
import com.isidroid.b21.R
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.math.max

val NavController?.hasHome get() = this?.backQueue?.any { it.destination.id == R.id.fragmentHome } == true

fun bitmapToByteArray(bitmap: Bitmap, width: Float, height: Float): ByteArray? {
    return try {
        val max = max(width, height).toInt()
        val stream = ByteArrayOutputStream();
        scaleToMaxSize(bitmap, max / 3, false).compress(Bitmap.CompressFormat.JPEG, 70, stream);
        val byteArray = stream.toByteArray();
        bitmap.recycle()
        byteArray
    } catch (t: Throwable) {
        null
    }
}

fun scaleToMaxSize(
    bitmap: Bitmap,
    maxSize: Int,
    scaleUp: Boolean = false
): Bitmap {
    val scale = java.lang.Float.min(maxSize.toFloat() / bitmap.width, maxSize.toFloat() / bitmap.height)
    if (scale > 1 && !scaleUp) return bitmap.copy(Bitmap.Config.ARGB_8888, true)

    val matrix = Matrix()
    matrix.postScale(scale, scale)

    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}

fun Bitmap.saveToFile(dest: File): Boolean {
    return try {
        dest.createNewFile()
        val bos = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 100, bos)

        val fos = FileOutputStream(dest)
        fos.write(bos.toByteArray())
        fos.flush()
        fos.close()
        true
    } catch (e: Exception) {
        Timber.e(e)
        false
    }
}

fun String.assetsFileContent(context: Context) = context.assets.open(this).bufferedReader().use { it.readText() }