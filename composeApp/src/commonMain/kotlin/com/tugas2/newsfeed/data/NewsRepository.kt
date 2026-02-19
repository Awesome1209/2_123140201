package com.tugas2.newsfeed.data

import com.tugas2.newsfeed.model.Category
import com.tugas2.newsfeed.model.News
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

class NewsRepository {

    private val titles = listOf(
        "Kotlin Coroutines Tips",
        "Startup Raises Funding",
        "Final Match Highlights",
        "Movie Premiere Buzz",
        "Stock Market Update",
        "New Tech Gadget Launch"
    )

    // (1) Flow: emit berita baru tiap 2 detik
    fun breakingNewsStream(): Flow<News> = flow {
        var counter = 1
        while (true) {
            delay(2.seconds)
            val category = Category.entries.random()
            val title = "${titles.random()} #$counter"
            emit(
                News(
                    id = "news-$counter",
                    title = title,
                    category = category,
                    timestampMs = System.currentTimeMillis()
                )
            )
            counter++
        }
    }

    // (5) Coroutine async: simulasi fetch detail berita (seperti API)
    suspend fun fetchNewsDetail(id: String): String {
        delay(Random.nextLong(300, 900))
        return "Detail untuk $id: ini isi berita versi panjang (simulasi)."
    }
}
