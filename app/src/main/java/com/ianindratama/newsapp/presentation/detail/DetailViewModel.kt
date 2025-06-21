package com.ianindratama.newsapp.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ianindratama.newsapp.core.domain.usecase.NewsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest

class DetailViewModel(newsId: Long, private val newsUseCase: NewsUseCase) : ViewModel() {

    private val favoriteNewsId = MutableStateFlow(newsId)

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val favoriteNews = favoriteNewsId
        .debounce(300)
        .distinctUntilChanged()
        .flatMapLatest {
            newsUseCase.getFavoriteNews(newsId)
        }
        .asLiveData()

    fun updateFavoriteNewsStatus() {
        favoriteNews.value?.let {
            val newIsFavorite = !it.isFavorite
            newsUseCase.setFavoriteNews(it.id, newIsFavorite)
        }
    }

}