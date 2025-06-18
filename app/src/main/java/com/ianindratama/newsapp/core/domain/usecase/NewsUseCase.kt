package com.ianindratama.newsapp.core.domain.usecase

import com.ianindratama.newsapp.core.data.Resource
import com.ianindratama.newsapp.core.domain.model.News
import kotlinx.coroutines.flow.Flow

interface NewsUseCase {
    fun getAllHighlightedNews(): Flow<Resource<List<News>>>
    fun getAllSearchedNews(query: String): Flow<Resource<List<News>>>
    fun getAllFavoriteNews(): Flow<List<News>>
    fun setFavoriteNews(news: News, newIsFavorite: Boolean)
}