package com.ianindratama.newsapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ianindratama.newsapp.core.domain.usecase.NewsUseCase

class HomeViewModel(newsUseCase: NewsUseCase) : ViewModel() {

    val news = newsUseCase.getAllNews().asLiveData()

}