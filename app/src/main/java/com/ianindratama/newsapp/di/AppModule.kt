package com.ianindratama.newsapp.di

import com.ianindratama.newsapp.core.domain.news.usecase.NewsInteractor
import com.ianindratama.newsapp.core.domain.news.usecase.NewsUseCase
import com.ianindratama.newsapp.core.domain.settings.usecase.UserSettingsInteractor
import com.ianindratama.newsapp.core.domain.settings.usecase.UserSettingsUseCase
import com.ianindratama.newsapp.presentation.detail.DetailViewModel
import com.ianindratama.newsapp.presentation.home.HomeViewModel
import com.ianindratama.newsapp.presentation.settings.SettingsViewModel
import com.ianindratama.newsapp.presentation.utils.DarkModeManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<NewsUseCase> { NewsInteractor(get()) }
    factory<UserSettingsUseCase> { UserSettingsInteractor(get()) }
}

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
val commonViewModelModule = module {
    viewModel { HomeViewModel(get()) }
}

val detailViewModelModule = module {
    viewModel { (newsId: Long) -> DetailViewModel(newsId, get()) }
}

val settingsViewModelModule = module {
    viewModel { SettingsViewModel(get()) }
}

val presentationModule = module {
    single { DarkModeManager(get()) }
}