package com.ianindratama.newsapp.core.utils

import com.ianindratama.newsapp.core.data.source.local.entity.NewsEntity
import com.ianindratama.newsapp.core.data.source.remote.response.NewsResponse
import com.ianindratama.newsapp.core.domain.model.News

object DataMapper {
    fun mapResponsesToEntities(input: List<NewsResponse>): List<NewsEntity> {
        val newsList = ArrayList<NewsEntity>()
        input.map {
            val news = NewsEntity(
                url = it.url ?: "",
                source = it.source?.name ?: "",
                publishedAt = it.publishedAt ?: "",
                author = it.author ?: "",
                urlToImage = it.urlToImage ?: "",
                description = it.description ?: "",
                title = it.title ?: "",
                content = it.content ?: "",
            )
            newsList.add(news)
        }
        return newsList
    }

    fun mapEntitiesToDomain(input: List<NewsEntity>): List<News> =
        input.map {
            mapEntityToDomain(it)
        }

    fun mapEntityToDomain(input: NewsEntity): News =
        News(
            id = input.id,
            url = input.url,
            source = input.source,
            publishedAt = input.publishedAt,
            author = input.author,
            urlToImage = input.urlToImage,
            description = input.description,
            title = input.title,
            content = input.content,
            isFavorite = input.isFavorite
        )
}