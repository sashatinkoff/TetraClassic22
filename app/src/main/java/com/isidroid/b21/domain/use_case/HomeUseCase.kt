package com.isidroid.b21.domain.use_case

import android.content.Context
import android.net.Uri
import com.isidroid.b21.data.source.local.AppDatabase
import com.isidroid.b21.domain.model.Post
import com.isidroid.b21.domain.repository.LiveJournalRepository
import com.isidroid.b21.domain.repository.PdfRepository
import com.isidroid.core.ext.date
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeUseCase @Inject constructor(
    private val context: Context,
    private val liveJournalRepository: LiveJournalRepository,
    private val pdfRepository: PdfRepository,
    private val appDatabase: AppDatabase
) {
    @Volatile
    private var isRunning = false

    fun start() = flow {
        isRunning = true
        var url = "https://fixin.livejournal.com/385.html"
//        url = liveJournalRepository.nextPostUrl("2033243")


//        url = "https://fixin.livejournal.com/2310772.html"
        val deadline = "2021-01-01".date

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

    fun createPdf(uri: Uri) = flow {
        pdfRepository.create(context, uri, object : PdfRepository.Listener {
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
}


// private
//        val post = liveJournalRepository.postHtml("https://fixin.livejournal.com/2310772.html")
// public
//        val post = liveJournalRepository.postHtml("https://fixin.livejournal.com/1196017.html")
//        liveJournalRepository.nextPost(post.id)

// https://www.livejournal.com/go.bml?journal=fixin&amp;itemid=1196017&amp;dir=next
// https://www.livejournal.com/go.bml?journal=fixin&amp;itemid=1196017&amp;dir=prev