package com.ianindratama.newsapp.core.data.source.local

import com.ianindratama.newsapp.core.data.model.NewsEntity
import com.ianindratama.newsapp.core.data.source.local.room.NewsDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val newsDao: NewsDao) {

    fun getAllHighlightedNews(): Flow<List<NewsEntity>> = newsDao.getAllHighlightedNews()

    fun getAllSearchedNews(search: String): Flow<List<NewsEntity>> = newsDao.getAllSearchedNews(search)

    fun getAllFavoriteNews(): Flow<List<NewsEntity>> = newsDao.getAllFavoriteNews()

    fun getNews(newsId: Long): Flow<NewsEntity> = newsDao.getNews(newsId)

    suspend fun insertNews(newsList: List<NewsEntity>) = newsDao.insertNews(newsList)

    fun setFavoriteNews(newsId: Long, newIsFavorite: Boolean) {
        newsDao.updateFavoriteTourism(newsId, newIsFavorite)
    }
}