package com.ianindratama.newsapp.core.utils

import com.ianindratama.newsapp.core.data.model.NewsEntity
import com.ianindratama.newsapp.core.data.source.remote.response.NewsResponse
import com.ianindratama.newsapp.core.domain.model.News
import com.ianindratama.newsapp.core.presentation.model.NewsUiModel

object NewsModelMapper {
    fun mapListResponseToListData(input: List<NewsResponse>): List<NewsEntity> {
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

    fun mapDataToDomain(input: NewsEntity): News =
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

    fun mapListDataToListDomain(input: List<NewsEntity>): List<News> =
        input.map {
            mapDataToDomain(it)
        }

    fun mapDomainToPresentation(input: News): NewsUiModel =
        NewsUiModel(
            id = input.id,
            url = input.url,
            source = input.source,
            publishedAt = input.publishedAt,
            finalAuthor = input.author.ifEmpty { input.source },
            urlToImage = input.urlToImage,
            description = input.description,
            title = input.title,
            finalContent = parseNewsFinalContent(input.content, input.description),
            isFavorite = input.isFavorite
        )

    fun mapListDomainToListPresentation(input: List<News>): List<NewsUiModel> =
        input.map {
            mapDomainToPresentation(it)
        }
}