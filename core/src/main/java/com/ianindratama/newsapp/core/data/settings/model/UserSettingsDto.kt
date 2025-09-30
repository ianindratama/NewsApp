package com.ianindratama.newsapp.core.data.settings.model

import kotlinx.serialization.Serializable

@Serializable
data class UserSettingsDto(
    val appThemeDto: AppThemeDto = AppThemeDto.SYSTEM
)