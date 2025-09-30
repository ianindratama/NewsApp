package com.ianindratama.newsapp.core.data.news.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ianindratama.newsapp.core.data.news.model.NewsEntity

@Database(entities = [NewsEntity::class], version = 1, exportSchema = false)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun newsDao(): NewsDao

}