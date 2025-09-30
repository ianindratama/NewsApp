package com.ianindratama.newsapp.core.domain.news.repository

import com.ianindratama.newsapp.core.utils.Resource
import com.ianindratama.newsapp.core.domain.news.model.News
import kotlinx.coroutines.flow.Flow

interface INewsRepository {

    fun getAllHighlightedNews(): Flow<Resource<List<News>>>

    fun getAllSearchedNews(search: String): Flow<Resource<List<News>>>

    fun getAllFavoriteNews(): Flow<List<News>>

    fun getNews(newsId: Long): Flow<News>

    fun setFavoriteNews(newsId: Long, newIsFavorite: Boolean)

}