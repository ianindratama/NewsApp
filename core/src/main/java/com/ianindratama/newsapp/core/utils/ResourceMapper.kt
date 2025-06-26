package com.ianindratama.newsapp.core.utils

import com.ianindratama.newsapp.core.domain.model.News
import com.ianindratama.newsapp.core.presentation.model.NewsUiModel

fun Resource<List<News>>.mapToNewsUiModel(): Resource<List<NewsUiModel>?> {
    return when (this) {
        is Resource.Success -> Resource.Success(
            this.data?.map { news ->
                NewsModelMapper.mapDomainToPresentation(news)
            }
        )
        is Resource.Error -> Resource.Error(
            this.message ?: "", null
        )
        is Resource.Loading -> Resource.Loading()
    }
}