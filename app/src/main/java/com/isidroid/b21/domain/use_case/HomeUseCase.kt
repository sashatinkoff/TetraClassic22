package com.isidroid.b21.domain.use_case

import com.isidroid.b21.domain.repository.LiveJournalRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeUseCase @Inject constructor(
    private val liveJournalRepository: LiveJournalRepository
) {
    fun start() = flow {
//        val content = liveJournalRepository.postHtml("https://fixin.livejournal.com/385.html")

        // private
        val post = liveJournalRepository.postHtml("https://fixin.livejournal.com/2310772.html")
//        val post = liveJournalRepository.postHtml("https://fixin.livejournal.com/1196017.html")
//        liveJournalRepository.nextPost(post.id)

        // https://www.livejournal.com/go.bml?journal=fixin&amp;itemid=1196017&amp;dir=next
        // https://www.livejournal.com/go.bml?journal=fixin&amp;itemid=1196017&amp;dir=prev

        emit(post)
    }
}

