package com.ianindratama.newsapp.core.domain.settings.usecase

import com.ianindratama.newsapp.core.domain.settings.model.UserSettings
import com.ianindratama.newsapp.core.domain.settings.repository.IUserSettingsRepository
import kotlinx.coroutines.flow.Flow

class UserSettingsInteractor(
    private val userSettingsRepository: IUserSettingsRepository
) : UserSettingsUseCase {
    override fun getUserSettings(): Flow<UserSettings> =
        userSettingsRepository.getUserSettings()

    override suspend fun setUserSettings(userSettings: UserSettings) {
        userSettingsRepository.setUserSettings(userSettings)
    }
}