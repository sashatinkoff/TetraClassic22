package com.isidroid.b21.data.repository

import androidx.core.net.toUri
import com.google.gson.Gson
import com.isidroid.b21.data.source.local.AppDatabase
import com.isidroid.b21.data.source.local.dao.PostDao
import com.isidroid.b21.data.source.remote.getCookie
import com.isidroid.b21.domain.model.Post
import com.isidroid.b21.domain.repository.LiveJournalRepository
import com.isidroid.core.ext.tryCatch
import org.jsoup.Jsoup
import java.text.SimpleDateFormat
import java.util.*

class LiveJournalRepositoryImpl(
    private val gson: Gson,
    private val appDatabase: AppDatabase
) : LiveJournalRepository {
    private val postDao: PostDao by lazy { appDatabase.postDao }

    override suspend fun postHtml(url: String): Post {
        val cookies: HashMap<String, String> = HashMap()

        getCookie(gson).forEach {
            cookies[it.name] = it.value
        }

        val html = Jsoup.connect(url)
            .cookies(cookies)
            .ignoreHttpErrors(true)
            .ignoreContentType(true)
            .execute()
            .body()

        return parsePost(html, getByUrl = url)
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
                getByUrl = getByUrl
            )
        } catch (t: Throwable) {
            Post(
                id = id,
                url = url,
                getByUrl = getByUrl
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
}