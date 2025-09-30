package com.ianindratama.newsapp.core.utils

import com.ianindratama.newsapp.core.data.settings.model.AppThemeDto
import com.ianindratama.newsapp.core.data.settings.model.UserSettingsDto
import com.ianindratama.newsapp.core.domain.settings.model.AppTheme
import com.ianindratama.newsapp.core.domain.settings.model.UserSettings
import com.ianindratama.newsapp.core.presentation.model.settings.AppThemeUiModel
import com.ianindratama.newsapp.core.presentation.model.settings.UserSettingsUiModel

object UserSettingsModelMapper {
    fun UserSettingsDto.toDomain() = UserSettings(
        appTheme = when (appThemeDto) {
            AppThemeDto.SYSTEM -> AppTheme.SYSTEM
            AppThemeDto.DARK -> AppTheme.DARK
            AppThemeDto.LIGHT -> AppTheme.LIGHT
        }
    )

    fun UserSettings.toDto() = UserSettingsDto(
        appThemeDto = when (appTheme) {
            AppTheme.SYSTEM -> AppThemeDto.SYSTEM
            AppTheme.DARK -> AppThemeDto.DARK
            AppTheme.LIGHT -> AppThemeDto.LIGHT
        }
    )

    fun UserSettings.toPresentation() = UserSettingsUiModel(
        appThemeUiModel = when (appTheme) {
            AppTheme.SYSTEM -> AppThemeUiModel.AUTO
            AppTheme.DARK -> AppThemeUiModel.ON
            AppTheme.LIGHT -> AppThemeUiModel.OFF
        }
    )

    fun UserSettingsUiModel.toDomain() = UserSettings(
        appTheme = when (appThemeUiModel) {
            AppThemeUiModel.AUTO -> AppTheme.SYSTEM
            AppThemeUiModel.ON -> AppTheme.DARK
            AppThemeUiModel.OFF -> AppTheme.LIGHT
        }
    )
}