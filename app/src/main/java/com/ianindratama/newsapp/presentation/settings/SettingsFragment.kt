package com.ianindratama.newsapp.presentation.settings

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.ianindratama.newsapp.R
import com.ianindratama.newsapp.core.presentation.model.settings.UserSettingsEvent
import com.ianindratama.newsapp.core.ui.utils.toAppThemeUiModel
import com.ianindratama.newsapp.core.ui.utils.toNightMode
import com.ianindratama.newsapp.presentation.MainActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class SettingsFragment : PreferenceFragmentCompat() {
    private val settingsViewModel: SettingsViewModel by viewModel()
    override fun onCreatePreferences(
        savedInstanceState: Bundle?,
        rootKey: String?
    ) {
        setPreferencesFromResource(R.xml.settings_preferences, rootKey)
        val prefListThemes =
            findPreference<ListPreference>(getString(R.string.pref_key_dark))

        prefListThemes?.setOnPreferenceChangeListener { _, newValue ->
            val value = when (newValue) {
                "on" -> getString(R.string.pref_dark_on)
                "off" -> getString(R.string.pref_dark_off)
                else -> getString(R.string.pref_dark_auto)
            }

            settingsViewModel.onUserSettingsEvent(
                UserSettingsEvent.OnAppThemeChanged(
                    value.uppercase(
                        Locale.US
                    ).toAppThemeUiModel()
                )
            )
            return@setOnPreferenceChangeListener true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MainActivity).updateAppBarTitle(getString(R.string.settings_fragment_title))

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(
                Lifecycle.State.RESUMED
            ) {
                settingsViewModel.userSettingsState.collectLatest { userSettings ->
                    if (userSettings != null) {
                        updateTheme(
                            userSettings.appThemeUiModel.toNightMode()
                        )
                    }
                }
            }
        }
    }

    private fun updateTheme(nightMode: Int) {
        if (AppCompatDelegate.getDefaultNightMode() != nightMode) {
            AppCompatDelegate.setDefaultNightMode(nightMode)
        }
    }
}