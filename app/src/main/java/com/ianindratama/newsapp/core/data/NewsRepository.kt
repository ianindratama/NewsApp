package com.ianindratama.newsapp.core.data

import androidx.room.Query
import com.ianindratama.newsapp.core.data.source.local.LocalDataSource
import com.ianindratama.newsapp.core.data.source.remote.RemoteDataSource
import com.ianindratama.newsapp.core.data.source.remote.network.ApiResponse
import com.ianindratama.newsapp.core.data.source.remote.response.NewsResponse
import com.ianindratama.newsapp.core.domain.model.News
import com.ianindratama.newsapp.core.domain.repository.INewsRepository
import com.ianindratama.newsapp.core.utils.AppExecutors
import com.ianindratama.newsapp.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NewsRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) : INewsRepository {
    override fun getAllNews(): Flow<Resource<List<News>>> =
        object : NetworkBoundResource<List<News>, List<NewsResponse>>(appExecutors) {
            override fun loadFromDB(): Flow<List<News>> {
                return localDataSource.getAllNews().map {
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override fun createCall(): Flow<ApiResponse<List<NewsResponse>>> =
                remoteDataSource.getAllNews()

            override suspend fun saveCallResult(data: List<NewsResponse>) {
                val newsList = DataMapper.mapResponsesToEntities(data)
                localDataSource.insertNews(newsList)
            }

            override fun shouldFetch(data: List<News>?): Boolean = data.isNullOrEmpty()
        }.asFlow()

    override fun getAllSearchedNews(search: String): Flow<Resource<List<News>>> =
        object : NetworkBoundResource<List<News>, List<NewsResponse>>(appExecutors) {
            override fun loadFromDB(): Flow<List<News>> {
                return localDataSource.getAllSearchedNews(search).map {
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override fun createCall(): Flow<ApiResponse<List<NewsResponse>>> =
                remoteDataSource.getAllSearchedNews(search)

            override suspend fun saveCallResult(data: List<NewsResponse>) {
                val newsList = DataMapper.mapResponsesToEntities(data)
                localDataSource.insertNews(newsList)
            }

            override fun shouldFetch(data: List<News>?): Boolean = data.isNullOrEmpty()
        }.asFlow()


    override fun getAllFavoriteNews(): Flow<List<News>> {
        return localDataSource.getAllFavoriteNews().map {
            DataMapper.mapEntitiesToDomain(it)
        }
    }

    override fun setFavoriteNews(news: News, newIsFavorite: Boolean) {
        val tourismEntity = DataMapper.mapDomainToEntity(news)
        appExecutors.diskIO().execute {
            localDataSource.setFavoriteNews(tourismEntity, newIsFavorite)
        }
    }
}