package com.tugas2.newsfeed.model

enum class Category { TECH, SPORTS, BUSINESS, ENTERTAINMENT }

data class News(
    val id: String,
    val title: String,
    val category: Category,
    val timestampMs: Long,
)

data class NewsUi(
    val id: String,
    val displayTitle: String,
    val categoryLabel: String,
)
