package com.ianindratama.newsapp.core.testutils

import com.ianindratama.newsapp.core.domain.news.model.News

fun news(
    id: Long = 1L,
    url: String = "https://example.com/$id",
    source: String = "Example",
    publishedAt: String = "2025-09-30T12:00:00Z",
    author: String = "Author",
    urlToImage: String = "https://example.com/$id.jpg",
    description: String = "desc $id",
    title: String = "title $id",
    content: String = "content $id",
    isFavorite: Boolean = false,
) = News(id, url, source, publishedAt, author, urlToImage, description, title, content, isFavorite)