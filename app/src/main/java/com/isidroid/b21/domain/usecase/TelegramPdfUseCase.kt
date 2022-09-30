package com.isidroid.b21.domain.usecase

import android.R
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.google.gson.Gson
import com.isidroid.b21.App
import com.isidroid.core.ext.fromJson
import com.isidroid.core.ext.readText
import com.isidroid.core.ext.tryCatch
import com.isidroid.core.utils.ResultData
import com.itextpdf.text.*
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfWriter
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.max


@Singleton
class TelegramPdfUseCase @Inject constructor(
    private val gson: Gson
) {

    fun create(uri: Uri) = flow {
        val context = App.instance.applicationContext

        val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm\nEEEE", Locale("ru", "RU"))
        val textSize = 10f

        val baseFont = BaseFont.createFont("/assets/font.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED)
        val font = Font(baseFont, textSize, Font.NORMAL)

        val hashTagFont = Font(baseFont, textSize, Font.NORMAL)
        hashTagFont.color = BaseColor.BLUE

        val baseMediumFont = BaseFont.createFont("/assets/medium.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED)
        val fontMedium = Font(baseMediumFont, textSize, Font.BOLD)

        val documentFile = DocumentFile.fromTreeUri(context, uri)!!
        val documentPhotoFile = documentFile.findFile("photos")

        val createFile = documentFile.createFile("text/pdf", "Diart.pdf")
        val fos = context.contentResolver.openOutputStream(createFile!!.uri)

        val document = Document()
        PdfWriter.getInstance(document, fos)
        document.open()
        document.pageSize = PageSize.A4;
        document.addCreationDate();


        val jsonFile = documentFile.findFile("result.json")!!
        val json = jsonFile.readText(context)
        val response = gson.fromJson<Response>(json)
        val messages = response.messages
            .filter { it.textEntities.any { it.type == "plain" } }


        messages.forEachIndexed { index, message ->
            val date = dateFormat.format(message.date)
            val text = message.textEntities.firstOrNull { it.type == "plain" }?.text.orEmpty()
            val hashtags = message.textEntities.filter { it.type == "hashtag" }.joinToString(", ") { it.text }

            document.add(Paragraph(date, fontMedium))
            document.add(Paragraph(text, font))

            if (hashtags.isNotBlank()) {
                document.add(Paragraph(hashtags, hashTagFont))
            }

            document.add(Paragraph("\n\n"))

            if (message.photo != null) {
                val url = message.photo.replace("photos/", "")
                val imageFile = documentPhotoFile!!.findFile(url)

                if (imageFile != null) {
                    val outputStream = tryCatch { context.contentResolver.openInputStream(imageFile.uri) }
                    val bitmap = BitmapFactory.decodeStream(outputStream)
                    if (bitmap != null) {
                        val byteArray = bitmapToByteArray(bitmap, document.pageSize.width, document.pageSize.height)
                        byteArray?.also { document.add(Image.getInstance(byteArray)) }
                    }
                }
            }

            emit(ResultData.Success(State.OnProgress(index + 1, messages.size)))
        }

        document.close()
        emit(ResultData.Success(State.OnComplete))
    }

    private fun bitmapToByteArray(bitmap: Bitmap, width: Float, height: Float): ByteArray? {
        return try {
            val max = max(width, height).toInt()
            val stream = ByteArrayOutputStream();
            scaleToMaxSize(bitmap, max / 2, false).compress(Bitmap.CompressFormat.PNG, 100, stream);
            val byteArray = stream.toByteArray();
            bitmap.recycle()
            byteArray
        } catch (t: Throwable) {
            null
        }
    }

    private fun getBitmap(path: String, width: Float, height: Float): ByteArray? {
        val context = App.instance
        val istr: InputStream
        val bitmap: Bitmap?
        return try {
            istr = context.assets.open(path)
            bitmap = BitmapFactory.decodeStream(istr)


            val max = max(width, height).toInt()
            val stream = ByteArrayOutputStream();
            scaleToMaxSize(bitmap, max / 2, false).compress(Bitmap.CompressFormat.PNG, 100, stream);
            val byteArray = stream.toByteArray();
            bitmap.recycle()

            byteArray
        } catch (e: IOException) {
            // handle exception
            null
        }
    }

    private fun scaleToMaxSize(
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
}