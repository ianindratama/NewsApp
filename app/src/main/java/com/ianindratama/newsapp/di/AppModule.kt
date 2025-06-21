package com.ianindratama.newsapp.di

import com.ianindratama.newsapp.core.domain.usecase.NewsInteractor
import com.ianindratama.newsapp.core.domain.usecase.NewsUseCase
import com.ianindratama.newsapp.presentation.home.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<NewsUseCase> { NewsInteractor(get()) }
}

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
}