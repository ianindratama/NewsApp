package com.ianindratama.newsapp.favorites.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ianindratama.newsapp.core.domain.usecase.NewsUseCase
import com.ianindratama.newsapp.core.utils.NewsModelMapper
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class FavoritesViewModel(newsUseCase: NewsUseCase) : ViewModel() {

    val favoritesNews = newsUseCase
        .getAllFavoriteNews().map {
            NewsModelMapper.mapListDomainToListPresentation(it)
        }
        .distinctUntilChanged()
        .asLiveData()

}