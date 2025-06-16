package com.ianindratama.newsapp.core.di

import androidx.room.Room
import com.ianindratama.newsapp.core.data.NewsRepository
import com.ianindratama.newsapp.core.data.source.local.LocalDataSource
import com.ianindratama.newsapp.core.data.source.local.room.NewsDatabase
import com.ianindratama.newsapp.core.data.source.remote.RemoteDataSource
import com.ianindratama.newsapp.core.data.source.remote.network.ApiService
import com.ianindratama.newsapp.core.domain.repository.INewsRepository
import com.ianindratama.newsapp.core.utils.AppExecutors
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            NewsDatabase::class.java, "News.db"
        ).fallbackToDestructiveMigration(false).build()
    }
    factory { get<NewsDatabase>().newsDao() }
}

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val repositoryModule = module {
    single { LocalDataSource(get()) }
    single { RemoteDataSource(get()) }
    factory { AppExecutors() }
    single<INewsRepository> { NewsRepository(get(), get(), get()) }
}