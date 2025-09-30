package com.ianindratama.newsapp.core.presentation.model.news

data class NewsUiModel(
    val id: Long,
    val url: String,
    val source: String,
    val publishedAt: String,
    val finalAuthor: String,
    val urlToImage: String,
    val description: String,
    val title: String,
    val finalContent: String,
    val isFavorite: Boolean
)