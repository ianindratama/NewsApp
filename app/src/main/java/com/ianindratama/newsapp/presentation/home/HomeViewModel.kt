package com.ianindratama.newsapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ianindratama.newsapp.core.domain.news.usecase.NewsUseCase
import com.ianindratama.newsapp.core.utils.mapToNewsUiModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge

@FlowPreview
@ExperimentalCoroutinesApi
class HomeViewModel(newsUseCase: NewsUseCase) : ViewModel() {
    private val listOfHighlightedNews = newsUseCase.getAllHighlightedNews().map { resource ->
        resource.mapToNewsUiModel()
    }

    private val _searchNewsQuery = MutableStateFlow("")
    val searchNewsQuery: StateFlow<String> = _searchNewsQuery

    private val listOfSearchedNews = searchNewsQuery
        .debounce(300)
        .distinctUntilChanged()
        .filter {
            it.trim().isNotEmpty()
        }.flatMapLatest {
            newsUseCase.getAllSearchedNews(it).map { resource ->
                resource.mapToNewsUiModel()
            }
        }

    val listOfNews = merge(listOfHighlightedNews, listOfSearchedNews)
        .distinctUntilChanged()
        .asLiveData()

    fun updateSearchQuery(newQuery: String) {
        _searchNewsQuery.value = newQuery
    }

    fun clearSearchQuery() {
        _searchNewsQuery.value = ""
    }
}