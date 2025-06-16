package com.ianindratama.newsapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ianindratama.newsapp.core.domain.usecase.NewsUseCase
import kotlinx.coroutines.flow.last

class MainViewModel(newsUseCase: NewsUseCase) : ViewModel() {

    val news = newsUseCase.getAllNews().asLiveData()

}