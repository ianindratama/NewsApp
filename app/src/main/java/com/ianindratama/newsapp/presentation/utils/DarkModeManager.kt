package com.ianindratama.newsapp.presentation.utils

import androidx.appcompat.app.AppCompatDelegate
import com.ianindratama.newsapp.core.domain.settings.usecase.UserSettingsUseCase
import com.ianindratama.newsapp.core.ui.utils.toNightMode
import com.ianindratama.newsapp.core.utils.UserSettingsModelMapper.toPresentation
import kotlinx.coroutines.flow.first

class DarkModeManager(
    private val userSettingsUseCase: UserSettingsUseCase
) {
    suspend fun applyAppThemeOnce() {
        val mode = userSettingsUseCase.getUserSettings().first().toPresentation().appThemeUiModel
        AppCompatDelegate.setDefaultNightMode(mode.toNightMode())
    }
}