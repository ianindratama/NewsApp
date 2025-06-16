package com.ianindratama.newsapp.core.domain.usecase

import com.ianindratama.newsapp.core.data.Resource
import com.ianindratama.newsapp.core.domain.model.News
import com.ianindratama.newsapp.core.domain.repository.INewsRepository
import kotlinx.coroutines.flow.Flow

class NewsInteractor(private val newsRepository: INewsRepository): NewsUseCase {
    override fun getAllNews(): Flow<Resource<List<News>>> {
        return newsRepository.getAllNews()
    }

    override fun getAllFavoriteNews(): Flow<List<News>> {
        return newsRepository.getAllFavoriteNews()
    }

    override fun setFavoriteNews(news: News, newIsFavorite: Boolean) {
        newsRepository.setFavoriteNews(news, newIsFavorite)
    }
}