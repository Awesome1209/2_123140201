package com.tugas2.newsfeed.presentation

import com.tugas2.newsfeed.data.NewsRepository
import com.tugas2.newsfeed.model.Category
import com.tugas2.newsfeed.model.News
import com.tugas2.newsfeed.model.NewsUi
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class NewsFeedViewModel(
    private val repo: NewsRepository,
    initialCategory: Category? = null
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val selectedCategory = MutableStateFlow<Category?>(initialCategory)

    private val _readCount = MutableStateFlow(0)
    val readCount: StateFlow<Int> = _readCount.asStateFlow()

    private val _feed = MutableStateFlow<List<NewsUi>>(emptyList())
    val feed: StateFlow<List<NewsUi>> = _feed.asStateFlow()

    private val _lastDetail = MutableStateFlow<String?>(null)
    val lastDetail: StateFlow<String?> = _lastDetail.asStateFlow()

    fun start() {
        scope.launch {
            repo.breakingNewsStream()
                .combine(selectedCategory) { news, cat ->
                    if (cat == null || news.category == cat) news else null
                }
                .filterNotNull()
                .map { it.toUi() }
                .onEach { ui ->
                    _feed.update { current ->
                        (listOf(ui) + current).take(50)
                    }
                }
                .catch { e ->
                    _lastDetail.value = "Error stream: ${e.message}"
                }
                .collect()
        }
    }

    fun setCategory(category: Category?) {
        selectedCategory.value = category
    }

    fun markAsRead(newsId: String) {
        _readCount.update { it + 1 }

        scope.launch {
            val detail = async { repo.fetchNewsDetail(newsId) }.await()
            _lastDetail.value = detail
        }
    }

    private fun News.toUi(): NewsUi {
        val label = category.name.lowercase().replaceFirstChar { it.uppercase() }
        return NewsUi(
            id = id,
            displayTitle = "[$label] $title",
            categoryLabel = label
        )
    }

    fun dispose() {
        scope.cancel()
    }
}
