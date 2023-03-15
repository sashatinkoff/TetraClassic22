package com.isidroid.b21.data.repository

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.isidroid.b21.App
import com.isidroid.b21.data.source.local.AppDatabase
import com.isidroid.b21.data.source.remote.api.ApiLiveJournal
import com.isidroid.b21.domain.repository.PdfRepository
import com.isidroid.b21.ext.bitmapToByteArray
import com.isidroid.b21.ext.saveToFile
import com.isidroid.core.ext.addMonths
import com.isidroid.core.ext.md5
import com.isidroid.core.ext.tryCatch
import com.itextpdf.text.*
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfWriter
import org.jsoup.Jsoup
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class PdfRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val apiLiveJournal: ApiLiveJournal
) : PdfRepository {
    private val postDao by lazy { appDatabase.postDao }

    override suspend fun create(context: Context, uri: Uri, listener: PdfRepository.Listener) {
        var date: Date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            .parse("2000-01-01 01:00:00")!!
        val yearFormat = SimpleDateFormat("yyyyy", Locale.getDefault())

        while (true) {
            val startDate = date
            val endDate = Date(date.addMonths(12).time - 1L)

            create(
                context = context,
                uri = uri,
                start = startDate,
                end = endDate,
                pdfFileName = "fixin",
                listener = listener
            )

            date = endDate

            if (yearFormat.format(date).toInt() > 2020) break
        }
    }

    override suspend fun create(context: Context, uri: Uri, start: Date, end: Date, pdfFileName: String, listener: PdfRepository.Listener) {
        val posts = postDao.filterByDate(start, end).filter { it.isDownloaded }
        if (posts.isEmpty())
            return

        val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm\nEEEE", Locale("ru", "RU"))
        val fileName = with(SimpleDateFormat("yyyy", Locale.getDefault())) {
            "${pdfFileName}_${format(start)}"
        }

        listener.startPdf(fileName)

        val textSize = 9f

        val baseFont = BaseFont.createFont("/assets/font.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED)
        val font = Font(baseFont, textSize, Font.NORMAL)

        val hashTagFont = Font(baseFont, textSize, Font.NORMAL)
        hashTagFont.color = BaseColor.BLUE

        val baseMediumFont = BaseFont.createFont("/assets/medium.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED)
        val fontMedium = Font(baseMediumFont, textSize, Font.BOLD)

        val documentFile = DocumentFile.fromTreeUri(context, uri)!!


        val createFile = documentFile.createFile("text/pdf", "$fileName.pdf")
        val fos = context.contentResolver.openOutputStream(createFile!!.uri)

        val document = Document()
        val pdfWriter = PdfWriter.getInstance(document, fos)
        document.open()
        document.pageSize = PageSize.A4;
        document.addCreationDate();

        posts.forEach { post ->
            val imageInsertion = "image_insertion_${UUID.randomUUID()}"
            val date = dateFormat.format(post.createdAt!!)

            var content = post.html.orEmpty().clearHtml()
            val jDoc = Jsoup.parse(content).normalise()

            arrayOf("span", "u", "b", "strong", "i", "table").forEach {
                jDoc.getElementsByTag(it).forEach { element -> content = content.replace("$element", element.text()) }
            }

            arrayOf("iframe").forEach {
                jDoc.getElementsByTag(it).forEach { element -> content = content.replace("$element", "") }
            }


            val images = mutableListOf<String>()
            // replace images
            jDoc.getElementsByTag("img").forEachIndexed { index, element ->
                val url = element.attr("src")
                content = content.replace("$element", imageInsertion)
                images.add(url)
            }

            jDoc.getElementsByTag("a").forEach { element ->
                content = content.replace("$element", element.attr("href"))
            }

            document.add(Paragraph("$date, ${post.url}\n${post.title}", fontMedium))

            val content2 = content.split(imageInsertion)

            content2.forEachIndexed { index, text ->
                document.add(Paragraph(text, font))

                val url = images.getOrNull(index)
                if (url != null) {
                    val imageFileName = "img_${url.md5()}"

                    val file = File(App.instance.cacheDir, imageFileName)
                    val bitmap = if (file.exists()) {
                        BitmapFactory.decodeFile(file.absolutePath)
                    } else {
                        tryCatch {
                            listener.downloadImage(url, post.title)

                            val body = apiLiveJournal.downloadFile(url).execute().body()
                            val outputStream = body?.byteStream()
                            BitmapFactory.decodeStream(outputStream)
                                .also { it.saveToFile(file) }
                        }
                    }

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

            if (post.isLiveJournal)
                document.add(Paragraph("\n"))

            listener.onPostSavedInPdf(post, fileName)
        }

        listener.pdfCompleted(fileName)
        document.close()
    }

    private fun String.clearHtml(): String {
        var content = replace("<html>\n", "").replace("</html>\n", "")
            .replace("<body>\n", "").replace("</body>\n", "")
            .replace("<head>\n", "").replace("</head>\n", "")
            .replace("<html>", "").replace("</html>", "")
            .replace("<body>", "").replace("</body>", "")
            .replace("<head>", "").replace("</head>", "")

            .replace("<p>", "").replace("</p>", "")
            .replace("<strong>", "").replace("</strong>", "")

            .replace("<br><br>", "")
            .replace("<br> <br>", "")
            .replace("<br />", "\n")
            .replace("<br>", "\n")
            .replace("<a name=\"cutid1\"></a>", "\n")
            .replace("&nbsp;", " ")

        content = "/\\s+/g".toRegex().replace(content, "\n")
        return content
    }
}