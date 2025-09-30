package com.ianindratama.newsapp.core.data.settings.repository

import androidx.datastore.core.DataStore
import com.ianindratama.newsapp.core.data.settings.model.UserSettingsDto
import com.ianindratama.newsapp.core.domain.settings.model.UserSettings
import com.ianindratama.newsapp.core.domain.settings.repository.IUserSettingsRepository
import com.ianindratama.newsapp.core.utils.UserSettingsModelMapper.toDomain
import com.ianindratama.newsapp.core.utils.UserSettingsModelMapper.toDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserSettingsRepository(
    private val dataStore: DataStore<UserSettingsDto>
) : IUserSettingsRepository {
    override fun getUserSettings(): Flow<UserSettings> = dataStore.data.map { it.toDomain() }

    override suspend fun setUserSettings(userSettings: UserSettings) {
        dataStore.updateData { userSettings.toDto() }
    }
}