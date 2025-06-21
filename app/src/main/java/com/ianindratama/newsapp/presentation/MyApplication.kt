package com.ianindratama.newsapp.presentation

import android.app.Application
import com.ianindratama.newsapp.core.di.databaseModule
import com.ianindratama.newsapp.core.di.networkModule
import com.ianindratama.newsapp.core.di.repositoryModule
import com.ianindratama.newsapp.di.useCaseModule
import com.ianindratama.newsapp.di.commonViewModelModule
import com.ianindratama.newsapp.di.detailViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApplication)
            modules(
                listOf(
                    databaseModule,
                    networkModule,
                    repositoryModule,
                    useCaseModule,
                    commonViewModelModule,
                    detailViewModelModule
                )
            )
        }
    }

}