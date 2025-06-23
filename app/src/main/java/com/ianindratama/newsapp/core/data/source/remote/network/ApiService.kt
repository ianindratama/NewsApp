package com.ianindratama.newsapp.core.data.source.remote.network

import com.ianindratama.newsapp.BuildConfig
import com.ianindratama.newsapp.core.data.source.remote.response.ListNewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("top-headlines")
    suspend fun getAllHighlightedNews(
        @Query("language") language: String = "en",
        @Query("apiKey") apiKey: String = BuildConfig.NEWS_API_KEY
    ): ListNewsResponse

    @GET("everything")
    suspend fun getAllSearchedNews(
        @Query("q") query: String = "cancer",
        @Query("apiKey") apiKey: String = BuildConfig.NEWS_API_KEY
    ) : ListNewsResponse

}