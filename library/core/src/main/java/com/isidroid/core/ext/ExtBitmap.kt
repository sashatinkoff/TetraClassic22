package com.isidroid.core.ext

import android.graphics.*
import android.graphics.Bitmap.CompressFormat
import timber.log.Timber
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.math.max
import kotlin.math.min


internal const val MAX_BITMAP_SIZE = 1920

val Bitmap.sizeString: String
    get() = "${width}x${height}"

fun Bitmap.scaleToMaxSize(
    maxSize: Int = MAX_BITMAP_SIZE,
    scaleUp: Boolean = false
): Bitmap {
    val scale = min(maxSize.toFloat() / width, maxSize.toFloat() / height)
    if (scale > 1 && !scaleUp) return this

    val matrix = Matrix()
    matrix.postScale(scale, scale)

    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

fun Bitmap.scaleToMaxVideoSize(frameSize: Int): Bitmap {
    val maxSize = 1080f
    val size = if (frameSize > maxSize) maxSize else frameSize.toFloat()
    val scale = size / height

    val matrix = Matrix()
    matrix.postScale(scale, scale)

    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}


fun Bitmap.rotateFitSize(degrees: Float): Bitmap {
    val targetBitmap = Bitmap.createBitmap(width, height, config)
    val canvas = Canvas(targetBitmap)
    val matrix = Matrix()
    matrix.setRotate(
        degrees, (width / 2).toFloat(),
        (height / 2).toFloat()
    )
    canvas.drawBitmap(this, matrix, Paint())
    return targetBitmap
}

fun Bitmap.rotateFullImage(degrees: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(degrees);
    val scaledBitmap = Bitmap.createScaledBitmap(this, width, height, true);
    return Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.width, scaledBitmap.height, matrix, true)
}

fun Bitmap.cropCenter(): Bitmap {
    val width: Int = width
    val height: Int = height
    val newWidth = if (height > width) width else height
    val newHeight = if (height > width) height - (height - width) else height
    var cropW = (width - height) / 2
    cropW = if (cropW < 0) 0 else cropW
    var cropH = (height - width) / 2
    cropH = if (cropH < 0) 0 else cropH
    val result = Bitmap.createBitmap(this, cropW, cropH, newWidth, newHeight)
    recycle()
    return result
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

fun Bitmap.inputStream(): ByteArrayInputStream? {
    return try {
        val bos = ByteArrayOutputStream()
        compress(CompressFormat.JPEG, 1, bos)
        val bitmapdata = bos.toByteArray()
        ByteArrayInputStream(bitmapdata)
    } catch (t: Throwable) {
        null
    }
}

fun Bitmap.mirror(): Bitmap {
    val matrix = Matrix()
    matrix.preScale(-1.0f, 1.0f)

    val result = Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    recycle()
    return result
}

fun Bitmap.addLabel(
    text: String,
): Bitmap {
    if (text.isBlank()) return this
    val percent = 0.05f

    val paintText = Paint().apply {
        isAntiAlias = true
        color = Color.parseColor("#FFFFFF")
        setShadowLayer(10.0f, 0.0f, 0.0f, Color.BLACK)
        textSize = width * percent
    }

    val bounds = Rect()
    paintText.getTextBounds(text, 0, text.length, bounds)

    val canvas = Canvas(this)
    val y = canvas.height - bounds.height() / 2
    val x = canvas.width / 2 - bounds.width() / 2

    canvas.drawText(text, x.toFloat(), y.toFloat(), paintText)
    return this
}


fun Bitmap.scale(
    leftEye: PointF,
    rightEye: PointF,
    prototypeLeftEye: PointF,
    prototypeRightEye: PointF
): Bitmap {
    val eyesDistance = prototypeLeftEye.x - prototypeRightEye.x
    val picEyesDistance = leftEye.x - rightEye.x
    val scale = eyesDistance / picEyesDistance

    if (scale == 1f) return this

    val width = width * scale
    val height = height * scale

    val overlay = Bitmap.createBitmap(width.toInt(), height.toInt(), Bitmap.Config.ARGB_8888)
    val canvas = Canvas(overlay)
    val matrix = Matrix().apply { setScale(scale, scale) }
    canvas.drawBitmap(Bitmap.createBitmap(this), matrix, null)

    return overlay
}

val Bitmap.maxSize get() = max(width, height)
val Bitmap.minSize get() = min(width, height)