package com.ianindratama.newsapp.core.data

import com.ianindratama.newsapp.core.data.source.local.LocalDataSource
import com.ianindratama.newsapp.core.data.source.remote.RemoteDataSource
import com.ianindratama.newsapp.core.data.source.remote.network.ApiResponse
import com.ianindratama.newsapp.core.data.source.remote.response.NewsResponse
import com.ianindratama.newsapp.core.domain.model.News
import com.ianindratama.newsapp.core.domain.repository.INewsRepository
import com.ianindratama.newsapp.core.utils.AppExecutors
import com.ianindratama.newsapp.core.utils.NewsModelMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NewsRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) : INewsRepository {
    override fun getAllHighlightedNews(): Flow<Resource<List<News>>> =
        object : NetworkBoundResource<List<News>, List<NewsResponse>>(appExecutors) {
            override fun loadFromDB(): Flow<List<News>> {
                return localDataSource.getAllHighlightedNews().map {
                    NewsModelMapper.mapListDataToListDomain(it)
                }
            }

            override fun createCall(): Flow<ApiResponse<List<NewsResponse>>> =
                remoteDataSource.getAllHighlightedNews()

            override suspend fun saveCallResult(data: List<NewsResponse>) {
                val newsList = NewsModelMapper.mapListResponseToListData(data)
                localDataSource.insertNews(newsList)
            }

            override fun shouldFetch(data: List<News>?): Boolean = data.isNullOrEmpty()
        }.asFlow()

    override fun getAllSearchedNews(search: String): Flow<Resource<List<News>>> =
        object : NetworkBoundResource<List<News>, List<NewsResponse>>(appExecutors) {
            override fun loadFromDB(): Flow<List<News>> {
                return localDataSource.getAllSearchedNews(search).map {
                    NewsModelMapper.mapListDataToListDomain(it)
                }
            }

            override fun createCall(): Flow<ApiResponse<List<NewsResponse>>> =
                remoteDataSource.getAllSearchedNews(search)

            override suspend fun saveCallResult(data: List<NewsResponse>) {
                val newsList = NewsModelMapper.mapListResponseToListData(data)
                localDataSource.insertNews(newsList)
            }

            override fun shouldFetch(data: List<News>?): Boolean = true
        }.asFlow()


    override fun getAllFavoriteNews(): Flow<List<News>> {
        return localDataSource.getAllFavoriteNews().map {
            NewsModelMapper.mapListDataToListDomain(it)
        }
    }

    override fun getNews(newsId: Long): Flow<News> {
        return localDataSource.getNews(newsId).map {
            NewsModelMapper.mapDataToDomain(it)
        }
    }

    override fun setFavoriteNews(newsId: Long, newIsFavorite: Boolean) {
        appExecutors.diskIO().execute {
            localDataSource.setFavoriteNews(newsId, newIsFavorite)
        }
    }
}