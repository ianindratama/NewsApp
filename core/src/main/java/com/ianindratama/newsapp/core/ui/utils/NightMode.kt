package com.ianindratama.newsapp.core.ui.utils

import androidx.appcompat.app.AppCompatDelegate
import com.ianindratama.newsapp.core.presentation.model.settings.AppThemeUiModel

fun AppThemeUiModel.toNightMode(): Int = when (this) {
    AppThemeUiModel.AUTO -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    AppThemeUiModel.ON   -> AppCompatDelegate.MODE_NIGHT_YES
    AppThemeUiModel.OFF  -> AppCompatDelegate.MODE_NIGHT_NO
}

fun String.toAppThemeUiModel(): AppThemeUiModel = when (this) {
    "ON" -> AppThemeUiModel.ON
    "OFF" -> AppThemeUiModel.OFF
    else -> AppThemeUiModel.AUTO
}