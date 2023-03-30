package com.isidroid.b21.domain.use_case

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.isidroid.b21.data.source.local.AppDatabase
import com.isidroid.b21.data.source.remote.api.ApiLiveJournal
import com.isidroid.b21.domain.model.ImageInfo
import com.isidroid.b21.domain.model.Post
import com.isidroid.b21.domain.repository.LiveJournalRepository
import com.isidroid.b21.domain.repository.PdfRepository
import com.isidroid.b21.domain.repository.PostRepository
import com.isidroid.core.ext.date
import com.isidroid.core.ext.md5
import com.isidroid.core.ext.tryCatch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.jsoup.Jsoup
import timber.log.Timber
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeUseCase @Inject constructor(
    private val context: Context,
    private val liveJournalRepository: LiveJournalRepository,
    private val pdfRepository: PdfRepository,
    private val appDatabase: AppDatabase,
    private val postRepository: PostRepository,
    private val apiLiveJournal: ApiLiveJournal
) {
    @Volatile
    private var isRunning = false

    fun start() = flow {
        isRunning = true

//        url = liveJournalRepository.nextPostUrl("2033243")
//        url = "https://fixin.livejournal.com/2310772.html"

        val deadline = "2021-01-01".date

        var url = postRepository.findLast()?.url ?: "https://fixin.livejournal.com/385.html"

        while (true) {
            if (!isRunning) break

            val dbPost = liveJournalRepository.findPostByUrl(url)

            if (dbPost?.isDownloaded == true) {
                emit(Result.OnPostFoundLocal(dbPost))
                url = liveJournalRepository.nextPostUrl(dbPost.id)
                continue
            }

            emit(Result.OnLoading(url))
            val post = liveJournalRepository.postHtml(url)


            emit(Result.OnPostSaved(post))

            url = liveJournalRepository.nextPostUrl(post.id)

            if (post.createdAt?.after(deadline) == true)
                break
        }
    }

    fun liveInternet() = flow {
        liveJournalRepository.loadLiveInternet()
        emit(true)
    }

    fun stop() {
        isRunning = false
    }

    fun createPdf(uri: Uri, imagesUri: Uri?) = flow {
        pdfRepository.create(context, uri, imagesUri, object : PdfRepository.Listener {
            override suspend fun startPdf(fileName: String) {
                emit(Result.StartPdf(fileName))
            }

            override suspend fun downloadImage(url: String, title: String?) {
                emit(Result.DownloadImage(url, title))
            }

            override suspend fun pdfCompleted(fileName: String) {
                emit(Result.PdfCompleted(fileName))
            }

            override suspend fun onPostSavedInPdf(post: Post, fileName: String) {
                emit(Result.PostSavedInPdf(post, fileName))
            }
        })
        emit(true)
    }

    fun stats(logs: MutableList<String>, event: String) = flow {
        val liveJournalCount = appDatabase.postDao.countBySource("LiveJournal")
        val liveInternetCount = appDatabase.postDao.countBySource("liveInternet")
        val liveJournalDownloaded = appDatabase.postDao.countBySourceDownloaded("LiveJournal")

        logs.add(0, event)

        val limited = logs.distinct().take(50)

        emit(
            Result.Stats(
                liveJournalCount = liveJournalCount,
                liveInternetCount = liveInternetCount,
                liveJournalDownloaded = liveJournalDownloaded,
                logs = limited
            )
        )
    }

    fun deleteLiveInternet() = flow {
        appDatabase.postDao.deleteLiveInternet()
        emit(true)
    }

    fun saveLjJson(uri: Uri) = flow {
        liveJournalRepository.saveToJson(uri)
        emit(true)
    }

    fun saveLjDatabase() = flow {
        liveJournalRepository.saveJsonInDatabase()
        emit(true)
    }

    fun loadImages(uri: Uri) = flow {
        Timber.i("start")

        val posts = postRepository.findAll()
        val images = mutableSetOf<String>()

        emit(ImageDownloadResult.Loading(message = "Parsing posts ${posts.size}"))

        for (post in posts) {
            val jDoc = Jsoup.parse(post.html.orEmpty()).normalise()
            val localImages = jDoc.getElementsByTag("img").map { element -> element.attr("src") }

            images.addAll(localImages)
        }

        val documentFolder = DocumentFile.fromTreeUri(context, uri)!!
        val existingFiles = documentFolder.listFiles()
            .filter { it.length() > 0 }
            .mapNotNull { it.name }
            .toMutableList()

        emit(ImageDownloadResult.Loading(message = "Local images found ${existingFiles.size}"))

        appDatabase.imagesDao.deleteAll()
        appDatabase.imagesDao.insert(existingFiles.map { ImageInfo(it) })

        val images2 = images
            .filter {
                val fileName = "img_${it.md5()}.jpg"
                !existingFiles.contains(fileName)
            }

        val total = images.size
        val existing = existingFiles.size
        existingFiles.clear()

        var outputStream: OutputStream? = null
        var stream: InputStream? = null

        for ((index, url) in images2.withIndex()) {
            val imageFileName = "img_${url.md5()}.jpg"
            val message = StringBuilder("download ${existing + index}/$total")

            try {
                val documentFile = try {
                    documentFolder.createFile("image/*", imageFileName)!!
                } catch (t: Throwable) {
                    documentFolder.findFile(imageFileName)?.delete()
                    documentFolder.createFile("image/*", imageFileName)!!
                }

                val body = apiLiveJournal.downloadFile(url).execute().body()
                stream = body?.byteStream()
                outputStream = context.contentResolver.openOutputStream(documentFile.uri)
                outputStream?.write(stream?.readBytes())


            } catch (t: Throwable) {
                message.append("\nError $url")
                Timber.e(t)
            } finally {
                outputStream?.close()
                stream?.close()

                outputStream = null
                stream = null
                message.append("\nsuccess")
            }

            emit(ImageDownloadResult.Loading(message = message.toString()))
        }

        emit(ImageDownloadResult.Complete(images.size))
    }
        .flowOn(Dispatchers.IO)

    sealed interface Result {
        data class OnPostFoundLocal(val post: Post) : Result
        data class OnLoading(val url: String) : Result
        data class OnPostSaved(val post: Post) : Result
        data class StartPdf(val fileName: String) : Result
        data class DownloadImage(val url: String, val title: String?) : Result
        data class PdfCompleted(val fileName: String) : Result
        data class Stats(val liveJournalCount: Int, val liveInternetCount: Int, val logs: List<String>, val liveJournalDownloaded: Int) : Result
        data class PostSavedInPdf(val post: Post, val fileName: String) : Result
    }

    sealed interface ImageDownloadResult {
        data class Loading(val message: String) : ImageDownloadResult
        data class Complete(val size: Int) : ImageDownloadResult
    }
}


// private
//        val post = liveJournalRepository.postHtml("https://fixin.livejournal.com/2310772.html")
// public
//        val post = liveJournalRepository.postHtml("https://fixin.livejournal.com/1196017.html")
//        liveJournalRepository.nextPost(post.id)

// https://www.livejournal.com/go.bml?journal=fixin&amp;itemid=1196017&amp;dir=next
// https://www.livejournal.com/go.bml?journal=fixin&amp;itemid=1196017&amp;dir=prev