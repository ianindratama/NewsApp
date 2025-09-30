package com.ianindratama.newsapp.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ianindratama.newsapp.core.domain.settings.usecase.UserSettingsUseCase
import com.ianindratama.newsapp.core.presentation.model.settings.AppThemeUiModel
import com.ianindratama.newsapp.core.presentation.model.settings.UserSettingsEvent
import com.ianindratama.newsapp.core.presentation.model.settings.UserSettingsUiModel
import com.ianindratama.newsapp.core.utils.UserSettingsModelMapper.toDomain
import com.ianindratama.newsapp.core.utils.UserSettingsModelMapper.toPresentation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SettingsViewModel(private val userSettingsUseCase: UserSettingsUseCase) : ViewModel() {
    private val _userSettingsState: MutableStateFlow<UserSettingsUiModel?> = MutableStateFlow(null)
    val userSettingsState = _userSettingsState.asStateFlow()

    init {
        viewModelScope.launch {
            userSettingsUseCase
                .getUserSettings()
                .map { it.toPresentation() }
                .distinctUntilChanged()
                .collectLatest { userSettings ->
                    _userSettingsState.value = userSettings
                }
        }
    }

    fun onUserSettingsEvent(event: UserSettingsEvent) {
        when (event) {
            is UserSettingsEvent.OnAppThemeChanged -> setNightMode(event.appThemeUiModel)
        }
    }

    private fun setNightMode(appThemeUiModel: AppThemeUiModel) = viewModelScope.launch {
        _userSettingsState.value?.let { userSettings ->
            userSettingsUseCase.setUserSettings(
                userSettings.copy(appThemeUiModel = appThemeUiModel).toDomain()
            )
        }
    }
}