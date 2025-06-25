package com.ianindratama.newsapp.favorites.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ianindratama.newsapp.core.domain.usecase.NewsUseCase
import kotlinx.coroutines.flow.distinctUntilChanged

class FavoritesViewModel(newsUseCase: NewsUseCase) : ViewModel() {

    val favoritesNews = newsUseCase
        .getAllFavoriteNews()
        .distinctUntilChanged()
        .asLiveData()

}