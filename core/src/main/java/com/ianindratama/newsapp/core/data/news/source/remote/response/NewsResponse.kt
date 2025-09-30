package com.ianindratama.newsapp.core.data.news.source.remote.response

import com.google.gson.annotations.SerializedName

data class NewsResponse(

    @field:SerializedName("url")
    val url: String?,

    @field:SerializedName("source")
    val source: NewsResponseSource?,

    @field:SerializedName("publishedAt")
    val publishedAt: String?,

    @field:SerializedName("author")
    val author: String?,

    @field:SerializedName("urlToImage")
    val urlToImage: String?,

    @field:SerializedName("description")
    val description: String?,

    @field:SerializedName("title")
    val title: String?,

    @field:SerializedName("content")
    val content: String?
)

data class NewsResponseSource(

    @field:SerializedName("name")
    val name: String?,
)
