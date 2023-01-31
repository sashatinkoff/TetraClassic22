package com.isidroid.b21.data.repository

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import com.google.gson.Gson
import com.isidroid.b21.data.mapper.PostMapper
import com.isidroid.b21.data.source.local.AppDatabase
import com.isidroid.b21.data.source.local.dao.PostDao
import com.isidroid.b21.data.source.remote.getCookie
import com.isidroid.b21.data.source.remote.response.RssDocumentResponse
import com.isidroid.b21.domain.model.Post
import com.isidroid.b21.domain.repository.LiveJournalRepository
import com.isidroid.b21.ext.assetsFileContent
import com.isidroid.core.ext.*
import org.jsoup.Jsoup
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class LiveJournalRepositoryImpl(
    private val gson: Gson,
    private val appDatabase: AppDatabase,
    private val context: Context
) : LiveJournalRepository {
    private val postDao: PostDao by lazy { appDatabase.postDao }

    override suspend fun postHtml(url: String, attempt: Int): Post {
        try {
            val cookies: HashMap<String, String> = HashMap()

            getCookie(gson).forEach {
                cookies[it.name] = it.value
            }

            val html = Jsoup.connect(url)
                .cookies(cookies)
                .ignoreHttpErrors(true)
                .ignoreContentType(true)
                .timeout(60_000)
                .execute()
                .body()

            return parsePost(html, getByUrl = url)
        } catch (t: Throwable) {
            if (attempt >= 10) throw t

            return postHtml(url, attempt + 1)
        }
    }

    override suspend fun nextPostUrl(postId: String, isNext: Boolean): String {
        return String.format(
            "https://www.livejournal.com/go.bml?journal=fixin&amp;itemid=%s&amp;dir=%s",
            postId,
            if (isNext) "next" else "prev"
        )
    }

    override suspend fun parsePost(html: String, getByUrl: String): Post {
//        val file = File(App.instance.cacheDir, "download_file.html")
//        file.bufferedWriter().use { out -> out.write(html) }

        Timber.i("$getByUrl")

        val document = Jsoup.parse(html)
        val url = document.getElementsByTag("meta").firstOrNull { it.attr("property") == "og:url" }?.attr("content")!!
        val id = url.toUri().lastPathSegment?.let { "\\D".toRegex().replace(it, "") }.orEmpty()

        val post = try {
            val dateFormats = arrayOf(
                "MMMM dd yyyy, HH:mm",
                "yyyy-MM-dd HH:mm:ss"
            ).map { SimpleDateFormat(it, Locale.getDefault()) }

            val timeDocument = document.body().getElementsByTag("time").first()!!

            val dateString = timeDocument.text()
            val date = dateFormats.firstNotNullOfOrNull { tryCatch { it.parse(dateString) } }!!

            val title = document.getElementsByTag("h1").firstOrNull()?.text()

            val content = arrayOf(
                document.getElementsByClass("aentry-post__text aentry-post__text--view").last(),
                document.getElementsByTag("article").last(),
                document.getElementsByTag("h1").first()
            ).firstNotNullOf { if (it?.text().isNullOrEmpty()) null else it }

            val contentHtml = content.html()
            val contentText = content.text()

            Post(
                id = id,
                createdAt = date,
                title = title,
                url = url,
                html = contentHtml,
                text = contentText,
                isDownloaded = true,
                getByUrl = getByUrl,
                source = "LiveJournal"
            )
        } catch (t: Throwable) {
            Post(
                id = id,
                url = url,
                getByUrl = getByUrl,
                source = "LiveJournal"
            )
        }

        // update if necessary
        val dbPost = postDao.findById(id = id)
        if (dbPost?.isDownloaded != true && post.isDownloaded)
            postDao.insert(post)

        return post
    }

    override suspend fun findPostById(id: String): Post? = postDao.findById(id)
    override suspend fun findPostByUrl(url: String): Post? = postDao.findByUrl(url) ?: postDao.findByGetByUrl(url)

    override suspend fun loadLiveInternet() {
        val files = arrayOf(
            "00.winter_2003",
            "01._spring_2003",
            "02._summer_2003",
            "03._autumn_2003",
            "04._winter_2004",
            "05._spring_2004",
            "06._summer_2004",
            "07.autumn_2004",
            "08.winter_2005",
            "09.spring_2005",
            "10.summer_2005",
//            "11.autumn_2005",
            "12.winter_2006",
            "13.spring_2006",
            "14.summer_2006",
            "15.autumn_2006",
            "16.winter_2007",
            "17.spring_2007-1",
            "18.spring_2007",
            "19.summer_2007-1",
            "20.summer_2007",
            "21.autumn_2007-1",
            "22.autumn_2007",
            "23.winter_2008-1",
            "24.winter_2008",
            "25.spring_2008",
            "26.summer_2008",
            "27.autumn_2008",
            "28.winter_2009"
        )

        files.forEachIndexed { filePosition, fileName ->
            val file = "$fileName.txt"
            val json = file.assetsFileContent(context)
            val data = gson.fromJson<RssDocumentResponse>(json)

            val posts = data.rss.channel.items.map { PostMapper.transformNetwork(it) }
            postDao.insert(*posts.toTypedArray())
        }
    }

    override suspend fun saveToJson(uri: Uri) {
        var date: Date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            .parse("2000-01-01 01:00:00")!!
        val yearFormat = SimpleDateFormat("yyyyy", Locale.getDefault())

        while (true) {
            val startDate = date
            val endDate = Date(date.addMonths(12).time - 1L)

            val posts = postDao.filterByDate(startDate, endDate).filter { it.isDownloaded }
            if (posts.isNotEmpty()) {
                val json = gson.toJson(posts)
                val file = File(context.cacheDir, "${UUID.randomUUID()}")
                file.saveString(json)

                file.copyToPublicFolder(context, targetDisplayName = "diary_${yearFormat.format(startDate)}.json", destUri = uri)
            }

            date = endDate

            if (yearFormat.format(date).toInt() > 2020) break
        }
    }
}