package com.ianindratama.newsapp.favorites.di

import com.ianindratama.newsapp.favorites.presentation.FavoritesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favoritesViewModelModule = module {
    viewModel { FavoritesViewModel(get()) }
}