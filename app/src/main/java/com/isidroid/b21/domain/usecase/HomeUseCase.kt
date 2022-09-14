package com.isidroid.b21.domain.usecase

import com.google.gson.Gson
import com.isidroid.b21.App
import com.isidroid.b21.data.mapper.PostMapper
import com.isidroid.b21.domain.model.RssDocumentResponse
import com.isidroid.b21.domain.repository.TestRepository
import com.isidroid.b21.ext.assetsFileContent
import com.isidroid.b21.ui.home.State
import com.isidroid.core.ext.fromJson
import com.isidroid.core.utils.ResultData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random


@Singleton
class HomeUseCase @Inject constructor(
    private val testRepository: TestRepository,
    private val gson: Gson
) {

    fun sendMessages() = flow {
//        testRepository.sendMessage(message = "Сегодня получил свой заказ - 9 наливных мужских ароматов от фирмы RENI, франция http://www.parfum21.com/ <br /> Дешевка 4 руб/мл, но зато хоть понюхаю, чем классика пахнет типа XS, Egoist<br /> Три дамских пробника приложили бесплатно, гы...<br /> Такие дела.<br /> Вечер свободный - делать нечево, буду типа отдыхать... ")

//        testRepository.sendMessage("Hello<br />woirk <img src=\"//i.li.ru//images/newsmiles/1/148.gif\" /><ul><li>It;s li</li></ul>")

        val files = arrayOf(
            "00.winter_2003",
//            "01._spring_2003", "02._summer_2003",
//            "03._autumn_2003", "04._winter_2004", "05._spring_2004",
//            "06._summer_2004", "07.autumn_2004", "08.winter_2005",
//            "09.spring_2005", "10.summer_2005", "11.autumn_2005",
//            "12.winter_2006", "13.spring_2006", "14.summer_2006",
//            "15.autumn_2006", "16.winter_2007", "17.spring_2007-1",
//            "18.spring_2007", "19.summer_2007-1", "20.summer_2007",
//            "21.autumn_2007-1", "22.autumn_2007", "23.winter_2008-1",
//            "24.winter_2008", "25.spring_2008", "26.summer_2008",
//            "27.autumn_2008", "28.winter_2009"
        )


        files.forEachIndexed { filePosition, fileName ->
            val file = "$fileName.json"
            val json = file.assetsFileContent(App.instance)
            val data = gson.fromJson<RssDocumentResponse>(json)

            val posts = data.rss.channel.items.map { PostMapper.transformNetwork(it) }.sortedBy { it.createdAt }
            posts.forEachIndexed { index, post ->
                val isCached = testRepository.sendMessage(post)

                emit(
                    ResultData.Success(
                        State.OnProgress(
                            filesCount = files.size,
                            currentFile = filePosition,
                            postsInFileCount = posts.size,
                            currentPost = index,
                            post = post
                        )
                    )
                )

                if (!isCached)
                    delay(Random.nextLong(2_000, 10_000))
            }
        }

        emit(ResultData.Success(State.OnComplete))
    }
}


