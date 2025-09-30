package com.ianindratama.newsapp.core.presentation.model.settings

data class UserSettingsUiModel(
    val appThemeUiModel: AppThemeUiModel = AppThemeUiModel.AUTO
)

sealed interface UserSettingsEvent {
    data class OnAppThemeChanged(val appThemeUiModel: AppThemeUiModel): UserSettingsEvent
}