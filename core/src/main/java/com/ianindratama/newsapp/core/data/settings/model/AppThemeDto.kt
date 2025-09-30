package com.ianindratama.newsapp.core.data.settings.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class AppThemeDto {
    @SerialName("system") SYSTEM,
    @SerialName("dark") DARK,
    @SerialName("light") LIGHT
}