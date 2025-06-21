package com.ianindratama.newsapp.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// TODO: Temporary - added parcelable for quick look - later, Create model in presentation layer to have parcelable
@Parcelize
data class News(
    val id: Long,
    val url: String,
    val source: String,
    val publishedAt: String,
    val author: String,
    val urlToImage: String,
    val description: String,
    val title: String,
    val content: String,
    val isFavorite: Boolean
) : Parcelable
