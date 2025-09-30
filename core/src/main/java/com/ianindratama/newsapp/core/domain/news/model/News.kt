package com.ianindratama.newsapp.core.domain.news.model

data class News(
    val id: Long,
    val url: String,
    val source: String,
    val publishedAt: String,
    val author: String,
    val urlToImage: String,
    val description: String,
    val title: String,
    val content: String,
    val isFavorite: Boolean
)