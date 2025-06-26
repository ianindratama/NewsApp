package com.ianindratama.newsapp.presentation.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.ianindratama.newsapp.R
import com.ianindratama.newsapp.presentation.MainActivity
import com.ianindratama.newsapp.presentation.utils.NightMode
import java.util.Locale

class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var prefManager: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onAttach(context: Context) {
        super.onAttach(context)
        prefManager = PreferenceManager.getDefaultSharedPreferences(requireContext())
        editor = prefManager.edit()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MainActivity).updateAppBarTitle(getString(R.string.settings_fragment_title))
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_preferences, rootKey)

        val prefListThemes = findPreference<ListPreference>(getString(R.string.pref_key_dark))
        prefListThemes?.setOnPreferenceChangeListener { _, newValue ->
            val value = when (newValue) {
                "on" -> getString(R.string.pref_dark_on)
                "off" -> getString(R.string.pref_dark_off)
                else -> getString(R.string.pref_dark_auto)
            }

            editor.putString(getString(R.string.pref_key_dark), value)
            editor.apply()
            val themeMode = NightMode.valueOf(value.uppercase(Locale.US)).value
            updateTheme(themeMode)

            return@setOnPreferenceChangeListener true
        }
    }

    private fun updateTheme(nightMode: Int): Boolean {
        AppCompatDelegate.setDefaultNightMode(nightMode)
        requireActivity().recreate()
        return true
    }

}