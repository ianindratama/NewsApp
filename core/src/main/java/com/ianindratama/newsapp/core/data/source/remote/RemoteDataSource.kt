package com.ianindratama.newsapp.core.data.source.remote

import com.ianindratama.newsapp.core.data.source.remote.network.ApiResponse
import com.ianindratama.newsapp.core.data.source.remote.network.ApiService
import com.ianindratama.newsapp.core.data.source.remote.response.NewsResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource(
    private val apiService: ApiService,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    fun getAllHighlightedNews(): Flow<ApiResponse<List<NewsResponse>>> {
        return flow {
            try {
                val response = apiService.getAllHighlightedNews()
                val dataArray = response.articles
                if (dataArray.isNotEmpty()) {
                    emit(ApiResponse.Success(response.articles))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(defaultDispatcher)
    }

    fun getAllSearchedNews(query: String): Flow<ApiResponse<List<NewsResponse>>> {
        return flow {
            try {
                val response = apiService.getAllSearchedNews(query)
                val dataArray = response.articles
                if (dataArray.isNotEmpty()) {
                    emit(ApiResponse.Success(response.articles))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(defaultDispatcher)
    }

}