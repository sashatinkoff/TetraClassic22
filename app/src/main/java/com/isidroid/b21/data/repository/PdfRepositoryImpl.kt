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
import com.isidroid.core.ext.string
import com.itextpdf.text.*
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfWriter
import org.jsoup.Jsoup
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class PdfRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val apiLiveJournal: ApiLiveJournal
) : PdfRepository {
    private val postDao by lazy { appDatabase.postDao }

    override suspend fun create(context: Context, uri: Uri) {
        var date: Date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            .parse("2005-01-01 01:00:00")!!
        val yearFormat = SimpleDateFormat("yyyyy", Locale.getDefault())

        while (true) {
            val startDate = date
            val endDate = Date(date.addMonths(12).time - 1L)

            create(
                context = context,
                uri = uri,
                start = startDate,
                end = endDate,
                pdfFileName = "fixin"
            )

            date = endDate

            if (yearFormat.format(date).toInt() > 2020) break
        }
    }

    override suspend fun create(context: Context, uri: Uri, start: Date, end: Date, pdfFileName: String) {
        val posts = postDao.filterByDate(start, end).filter { it.isDownloaded }
        if (posts.isEmpty())
            return

        val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm\nEEEE", Locale("ru", "RU"))
        val fileName = with(SimpleDateFormat("yyyy", Locale.getDefault())) {
            "${pdfFileName}_${format(start)}"
        }


        Timber.i("create ${start.string}-${end.string}, fileName=$fileName")


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
        PdfWriter.getInstance(document, fos)
        document.open()
        document.pageSize = PageSize.A4;
        document.addCreationDate();

        posts.forEach { post ->
            val imageInsertion = "image_insertion_${UUID.randomUUID()}"
            val date = dateFormat.format(post.createdAt!!)
            var content = post.html!!
                .replace("<br><br>", "")
                .replace("<br />", "\n")
                .replace("<br>", "\n")
                .replace("<p>", "")
                .replace("</p>", "")
                .replace("<a name=\"cutid1\"></a>", "\n")
                .replace("&nbsp;", " ")
                .replace("<strong>", "")
                .replace("</strong>", "")

            content = "/\\s+/g".toRegex().replace(content, "\n")

            val jDoc = Jsoup.parse(content).normalise()
            val images = mutableListOf<String>()

            arrayOf("span", "u", "b", "strong", "i").forEach {
                jDoc.getElementsByTag(it).forEach { element -> content = content.replace("$element", element.text()) }
            }

            arrayOf("iframe").forEach {
                jDoc.getElementsByTag(it).forEach { element -> content = content.replace("$element", "") }
            }

            // replace images
            jDoc.getElementsByTag("img").forEachIndexed { index, element ->
                val url = element.attr("src")
                content = content.replace("$element", imageInsertion)
                images.add(url)
            }


            jDoc.getElementsByTag("a").forEach { element ->
                content = content.replace("$element", element.attr("href"))
            }

            document.add(Paragraph("$date, ${post.url}", fontMedium))

            val content2 = content.split(imageInsertion)
            content2.forEachIndexed { index, text ->
                document.add(Paragraph(text, font))

                val url = images.getOrNull(index)
                if (url != null) {
                    val file = File(App.instance.cacheDir, url.md5())
                    val bitmap = if (file.exists()) {
                        BitmapFactory.decodeFile(file.absolutePath)
                    } else {
                        val body = apiLiveJournal.downloadFile(url).execute().body()
                        val outputStream = body?.byteStream()
                        BitmapFactory.decodeStream(outputStream)
                            .also { it.saveToFile(file) }
                    }

                    if (bitmap != null) {
                        val byteArray = bitmapToByteArray(bitmap, document.pageSize.width, document.pageSize.height)
                        byteArray?.also { document.add(Image.getInstance(byteArray)) }
                    }
                }
            }


            document.add(Paragraph("\n\n"))
        }

        document.close()
    }
}