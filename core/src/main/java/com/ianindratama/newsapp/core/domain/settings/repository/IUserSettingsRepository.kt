package com.ianindratama.newsapp.core.domain.settings.repository

import com.ianindratama.newsapp.core.domain.settings.model.UserSettings
import kotlinx.coroutines.flow.Flow

interface IUserSettingsRepository {

    fun getUserSettings(): Flow<UserSettings>

    suspend fun setUserSettings(userSettings: UserSettings)

}