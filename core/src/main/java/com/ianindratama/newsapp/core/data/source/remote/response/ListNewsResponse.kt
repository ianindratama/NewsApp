package com.ianindratama.newsapp.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ListNewsResponse(

	@field:SerializedName("articles")
	val articles: List<NewsResponse>
)
