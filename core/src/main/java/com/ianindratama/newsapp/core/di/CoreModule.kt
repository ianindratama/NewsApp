package com.ianindratama.newsapp.core.di

import androidx.datastore.core.DataStore
import androidx.room.Room
import com.ianindratama.newsapp.core.data.news.repository.NewsRepository
import com.ianindratama.newsapp.core.data.settings.repository.UserSettingsRepository
import com.ianindratama.newsapp.core.data.settings.model.UserSettingsDto
import com.ianindratama.newsapp.core.data.news.source.local.LocalDataSource
import com.ianindratama.newsapp.core.data.news.source.local.room.NewsDatabase
import com.ianindratama.newsapp.core.data.news.source.remote.RemoteDataSource
import com.ianindratama.newsapp.core.data.news.source.remote.network.ApiService
import com.ianindratama.newsapp.core.data.settings.source.local.datastore.UserSettingsStore.createUserSettingsDataStore
import com.ianindratama.newsapp.core.domain.news.repository.INewsRepository
import com.ianindratama.newsapp.core.domain.settings.repository.IUserSettingsRepository
import com.ianindratama.newsapp.core.utils.API_BASE_URL
import com.ianindratama.newsapp.core.utils.API_USER_AGENT_KEY
import com.ianindratama.newsapp.core.utils.API_USER_AGENT_VALUE
import com.ianindratama.newsapp.core.ui.utils.AppExecutors
import com.ianindratama.newsapp.core.utils.CERTIFICATE_1
import com.ianindratama.newsapp.core.utils.CERTIFICATE_2
import com.ianindratama.newsapp.core.utils.CERTIFICATE_3
import com.ianindratama.newsapp.core.utils.DATABASE_FILE_NAME
import com.ianindratama.newsapp.core.utils.HOST_NAME
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import okhttp3.CertificatePinner
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val databaseModule = module {
    single {
        val passphrase = SQLiteDatabase.getBytes("ianindratama-newsapp".toCharArray())
        val factory = SupportFactory(passphrase)
        Room.databaseBuilder(
            androidContext(),
            NewsDatabase::class.java, DATABASE_FILE_NAME
        ).fallbackToDestructiveMigration(false)
            .openHelperFactory(factory)
            .build()
    }
    factory { get<NewsDatabase>().newsDao() }
}

val networkModule = module {
    single {
        Interceptor { chain ->
            val request = chain.request().newBuilder()
                .header(API_USER_AGENT_KEY, API_USER_AGENT_VALUE)
                .build()
            chain.proceed(request)
        }
    }
    single {
        val certificatePinner = CertificatePinner.Builder()
            .add(HOST_NAME, CERTIFICATE_1)
            .add(HOST_NAME, CERTIFICATE_2)
            .add(HOST_NAME, CERTIFICATE_3)
            .build()
        OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .certificatePinner(certificatePinner)
            .addInterceptor(get())
            .build()
    }
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl(API_BASE_URL)
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

    single<DataStore<UserSettingsDto>> { createUserSettingsDataStore(androidContext()) }
    single<IUserSettingsRepository> { UserSettingsRepository(get()) }
}