package com.ianindratama.newsapp.core.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ianindratama.newsapp.core.data.source.local.entity.NewsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Query("SELECT * FROM news")
    fun getAllHighlightedNews(): Flow<List<NewsEntity>>

    @Query("SELECT * FROM news where title LIKE '%' || :search || '%' OR description LIKE '%' || :search || '%' OR content LIKE '%' || :search || '%'")
    fun getAllSearchedNews(search: String): Flow<List<NewsEntity>>

    @Query("SELECT * FROM news where isFavorite = 1")
    fun getAllFavoriteNews(): Flow<List<NewsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news: List<NewsEntity>)

    @Update
    fun updateFavoriteTourism(news: NewsEntity)

}