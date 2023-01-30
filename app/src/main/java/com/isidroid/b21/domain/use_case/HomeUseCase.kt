package com.isidroid.b21.domain.use_case

import com.isidroid.b21.domain.model.Post
import com.isidroid.b21.domain.repository.LiveJournalRepository
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeUseCase @Inject constructor(
    private val liveJournalRepository: LiveJournalRepository
) {
    @Volatile
    private var isRunning = false

    fun start() = flow {
        isRunning = true
        var url = "https://fixin.livejournal.com/385.html"

        while (true) {
            if (!isRunning) break

            val dbPost = liveJournalRepository.findPostByUrl(url)

            Timber.i("findPostByUrl $url, dbPost=${dbPost != null}")

            if (dbPost != null) {
                emit(Result.OnPostFoundLocal(dbPost))
                url = liveJournalRepository.nextPostUrl(dbPost.id)
                continue
            }

            emit(Result.OnLoading(url))
            val post = liveJournalRepository.postHtml(url)
            emit(Result.OnPostSaved(post))

            url = liveJournalRepository.nextPostUrl(post.id)
        }
    }

    fun stop() {
        isRunning = false
    }

    sealed interface Result {
        data class OnPostFoundLocal(val post: Post) : Result
        data class OnLoading(val url: String) : Result
        data class OnPostSaved(val post: Post)
    }
}


// private
//        val post = liveJournalRepository.postHtml("https://fixin.livejournal.com/2310772.html")
// public
//        val post = liveJournalRepository.postHtml("https://fixin.livejournal.com/1196017.html")
//        liveJournalRepository.nextPost(post.id)

// https://www.livejournal.com/go.bml?journal=fixin&amp;itemid=1196017&amp;dir=next
// https://www.livejournal.com/go.bml?journal=fixin&amp;itemid=1196017&amp;dir=prev