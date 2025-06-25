package com.ianindratama.newsapp.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ianindratama.newsapp.core.domain.usecase.NewsUseCase
import com.ianindratama.newsapp.core.utils.NewsDataMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class DetailViewModel(newsId: Long, private val newsUseCase: NewsUseCase) : ViewModel() {

    private val newsId = MutableStateFlow(newsId)

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val news = this.newsId
        .debounce(300)
        .distinctUntilChanged()
        .flatMapLatest {
            newsUseCase.getNews(newsId).map {
                NewsDataMapper.mapDomainToPresentation(it)
            }
        }
        .asLiveData()

    fun updateFavoriteNewsStatus() {
        news.value?.let {
            val newIsFavorite = !it.isFavorite
            newsUseCase.setFavoriteNews(it.id, newIsFavorite)
        }
    }

}