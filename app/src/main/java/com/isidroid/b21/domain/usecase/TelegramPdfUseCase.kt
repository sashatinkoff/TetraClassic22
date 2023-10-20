package com.isidroid.b21.domain.usecase

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.google.gson.Gson
import com.isidroid.core.ext.fromJson
import com.isidroid.core.ext.json
import com.isidroid.core.ext.readText
import com.isidroid.core.ext.tryCatch
import com.itextpdf.text.*
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfWriter
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.lang.Integer.min
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.max


@Singleton
class TelegramPdfUseCase @Inject constructor(
    private val gson: Gson,
    @ApplicationContext private val context: Context
) {

    fun create(uri: Uri, name: String) = flow {
        Timber.i("create $uri")

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

        var pdfFolder = documentFile.findFile("pdf")
        if (pdfFolder?.exists() != true) {
            pdfFolder = documentFile.createDirectory("pdf")!!
        }

        val existsFile = pdfFolder.findFile("$name.pdf")

        if (existsFile?.exists() == true) {
            existsFile.delete()
        }

        val createFile = pdfFolder.createFile("text/pdf", "$name.pdf")
        val fos = context.contentResolver.openOutputStream(createFile!!.uri)

        val document = Document()
        val pdfWriter = PdfWriter.getInstance(document, fos)
        document.pageSize = PageSize.A4

        document.open()
        document.addCreationDate();

        val messages = getMessages(documentFile)

        messages
            .forEachIndexed { index, message ->
                val date = dateFormat.format(Date(message.timestamp * 1000L))
                val text = message.textEntities.firstOrNull { it.type == "plain" }?.text.orEmpty()
                val hashtags = message.textEntities.filter { it.type == "hashtag" }.joinToString(", ") { it.text }

                document.add(Paragraph(date, fontMedium))
                document.add(Paragraph(text, font))

                if (hashtags.isNotBlank()) {
                    document.add(Paragraph(hashtags, hashTagFont))
                }

                for (photo in message.photoList.orEmpty()) {
                    val url = photo.replace("photos/", "")
                    val imageFile = documentPhotoFile!!.findFile(url)

                    if (imageFile != null) {
                        val outputStream = tryCatch { context.contentResolver.openInputStream(imageFile.uri) }
                        val bitmap = BitmapFactory.decodeStream(outputStream)
                        if (bitmap != null) {
                            val width = kotlin.math.min(bitmap.width.toFloat(), document.pageSize.width)
                            val height = kotlin.math.min(bitmap.height.toFloat(), document.pageSize.height)

                            val byteArray = bitmapToByteArray(bitmap, width, height)
                            val image = Image.getInstance(byteArray)
                            image.alignment = Image.ALIGN_MIDDLE

                            byteArray?.also {
                                val pos = pdfWriter.getVerticalPosition(false)
                                if (pos < image.height)
                                    document.newPage()

                                document.add(image)
                            }
                        }
                    }
                }


                document.add(Paragraph("\n\n"))

                emit(State.OnProgress(index + 1, messages.size))
            }

        document.close()
        emit(State.OnComplete)
    }

    private fun bitmapToByteArray(bitmap: Bitmap, width: Float, height: Float): ByteArray? {
        return try {
            val max = max(width, height).toInt()
            val stream = ByteArrayOutputStream();
            scaleToMaxSize(bitmap, max / 1.5f, false).compress(Bitmap.CompressFormat.PNG, 100, stream);
            val byteArray = stream.toByteArray();
            bitmap.recycle()
            byteArray
        } catch (t: Throwable) {
            null
        }
    }

    private fun scaleToMaxSize(
        bitmap: Bitmap,
        maxSize: Float,
        scaleUp: Boolean = false
    ): Bitmap {
        val scale = java.lang.Float.min(maxSize / bitmap.width, maxSize / bitmap.height)
        if (scale > 1 && !scaleUp) return bitmap.copy(Bitmap.Config.ARGB_8888, true)

        val matrix = Matrix()
        matrix.postScale(scale, scale)

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun getMessages(documentFile: DocumentFile): List<Response.MessageResponse> {
        val jsonFile = documentFile.findFile("result.json")!!
        val json = jsonFile.readText(context)
        val messagesResponse = gson.fromJson<Response>(json)
        val messages = messagesResponse.messages

        val dateMap = hashMapOf<Long, Response.MessageResponse>()
        for (m in messages) {
            if (!dateMap.containsKey(m.timestamp))
                dateMap[m.timestamp] = m

            if (m.photo != null) {
                if (dateMap[m.timestamp]?.photoList == null)
                    dateMap[m.timestamp]?.photoList = mutableSetOf()

                dateMap[m.timestamp]?.photoList?.add(m.photo)
            }

        }

        return dateMap.values.sortedBy { it.timestamp }
            .also {
                Timber.i("${it.last().json}")
                Timber.i("${it.last()}")
                Timber.i("${it.last().photoList}")
            }
    }



}