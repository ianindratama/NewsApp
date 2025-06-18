package com.ianindratama.newsapp.core.domain.repository

import com.ianindratama.newsapp.core.data.Resource
import com.ianindratama.newsapp.core.domain.model.News
import kotlinx.coroutines.flow.Flow

interface INewsRepository {

    fun getAllHighlightedNews(): Flow<Resource<List<News>>>

    fun getAllSearchedNews(search: String): Flow<Resource<List<News>>>

    fun getAllFavoriteNews(): Flow<List<News>>

    fun setFavoriteNews(news: News, newIsFavorite: Boolean)

}