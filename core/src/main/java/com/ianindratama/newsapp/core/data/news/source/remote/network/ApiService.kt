package com.ianindratama.newsapp.core.data.news.source.remote.network

import com.ianindratama.newsapp.core.BuildConfig
import com.ianindratama.newsapp.core.data.news.source.remote.response.ListNewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("top-headlines?language=en")
    suspend fun getAllHighlightedNews(
        @Query("apiKey") apiKey: String = BuildConfig.NEWS_API_KEY
    ): ListNewsResponse

    @GET("everything")
    suspend fun getAllSearchedNews(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String = BuildConfig.NEWS_API_KEY
    ) : ListNewsResponse

}