package com.ianindratama.newsapp.core.data.settings.source.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import com.ianindratama.newsapp.core.data.settings.model.UserSettingsDto
import com.ianindratama.newsapp.core.utils.USER_SETTINGS_FILE_NAME
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

object UserSettingsStore {
    fun createUserSettingsDataStore(appContext: Context): DataStore<UserSettingsDto> =
        DataStoreFactory.create(
            serializer = UserPreferencesSerializer,
            corruptionHandler = ReplaceFileCorruptionHandler { UserSettingsDto() },
            scope = CoroutineScope(SupervisorJob() + Dispatchers.IO),
            produceFile = { appContext.dataStoreFile(USER_SETTINGS_FILE_NAME) }
        )
}

