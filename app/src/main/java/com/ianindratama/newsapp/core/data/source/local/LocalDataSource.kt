package com.ianindratama.newsapp.core.data.source.local

import com.ianindratama.newsapp.core.data.source.local.entity.NewsEntity
import com.ianindratama.newsapp.core.data.source.local.room.NewsDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val newsDao: NewsDao) {

    fun getAllNews(): Flow<List<NewsEntity>> = newsDao.getAllNews()

    fun getAllSearchedNews(search: String): Flow<List<NewsEntity>> = newsDao.getSearchedNews(search)

    fun getAllFavoriteNews(): Flow<List<NewsEntity>> = newsDao.getFavoriteNews()

    suspend fun insertNews(newsList: List<NewsEntity>) = newsDao.insertNews(newsList)

    fun setFavoriteNews(newsEntity: NewsEntity, newIsFavorite: Boolean) {
        newsEntity.isFavorite = newIsFavorite
        newsDao.updateFavoriteTourism(newsEntity)
    }
}