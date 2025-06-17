package com.ianindratama.newsapp.di

import com.ianindratama.newsapp.core.domain.usecase.NewsInteractor
import com.ianindratama.newsapp.core.domain.usecase.NewsUseCase
import com.ianindratama.newsapp.presentation.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<NewsUseCase> { NewsInteractor(get()) }
}

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
}