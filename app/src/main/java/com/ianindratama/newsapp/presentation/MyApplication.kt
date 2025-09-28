package com.ianindratama.newsapp.presentation

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.ianindratama.newsapp.BuildConfig
import com.ianindratama.newsapp.R
import com.ianindratama.newsapp.core.di.databaseModule
import com.ianindratama.newsapp.core.di.networkModule
import com.ianindratama.newsapp.core.di.repositoryModule
import com.ianindratama.newsapp.di.useCaseModule
import com.ianindratama.newsapp.di.commonViewModelModule
import com.ianindratama.newsapp.di.detailViewModelModule
import com.ianindratama.newsapp.presentation.utils.NightMode
import leakcanary.LeakCanary
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import java.util.Locale

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

        if (BuildConfig.DEBUG) {
            LeakCanary.config = LeakCanary.config.copy(
                retainedVisibleThreshold = 3
            )
        }

        val preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        preferences.getString(
            getString(R.string.pref_key_dark),
            getString(R.string.pref_dark_auto)
        )?.apply {
            val mode = NightMode.valueOf(this.uppercase(Locale.US))
            AppCompatDelegate.setDefaultNightMode(mode.value)
        }
    }

}