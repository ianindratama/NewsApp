package com.ianindratama.newsapp.core.domain.settings.usecase

import com.ianindratama.newsapp.core.domain.settings.model.UserSettings
import kotlinx.coroutines.flow.Flow

interface UserSettingsUseCase {

    fun getUserSettings(): Flow<UserSettings>

    suspend fun setUserSettings(userSettings: UserSettings)

}