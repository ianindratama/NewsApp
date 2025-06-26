package com.ianindratama.newsapp.core.domain.usecase

import com.ianindratama.newsapp.core.utils.Resource
import com.ianindratama.newsapp.core.domain.model.News
import com.ianindratama.newsapp.core.domain.repository.INewsRepository
import kotlinx.coroutines.flow.Flow

class NewsInteractor(private val newsRepository: INewsRepository): NewsUseCase {
    override fun getAllHighlightedNews(): Flow<Resource<List<News>>> {
        return newsRepository.getAllHighlightedNews()
    }

    override fun getAllSearchedNews(query: String): Flow<Resource<List<News>>> {
        return newsRepository.getAllSearchedNews(query)
    }

    override fun getAllFavoriteNews(): Flow<List<News>> {
        return newsRepository.getAllFavoriteNews()
    }

    override fun getNews(newsId: Long): Flow<News> {
        return newsRepository.getNews(newsId)
    }

    override fun setFavoriteNews(newsId: Long, newIsFavorite: Boolean) {
        newsRepository.setFavoriteNews(newsId, newIsFavorite)
    }
}