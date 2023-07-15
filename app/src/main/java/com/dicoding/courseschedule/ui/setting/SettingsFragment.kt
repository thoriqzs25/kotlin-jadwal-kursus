package com.dicoding.courseschedule.ui.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.notification.DailyReminder

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        //TODO 10 : Update theme based on value in ListPreference
        val themePref = findPreference<ListPreference>(getString(R.string.pref_key_dark))

        themePref?.setOnPreferenceChangeListener { _, newValue ->
            when (newValue) {
                getString(R.string.pref_dark_on) -> updateTheme(AppCompatDelegate.MODE_NIGHT_YES)
                getString(R.string.pref_dark_off) -> updateTheme(AppCompatDelegate.MODE_NIGHT_NO)
                getString(R.string.pref_dark_auto) -> updateTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                else -> false
            }
        }
        //TODO 11 : Schedule and cancel notification in DailyReminder based on SwitchPreference
        val dailyReminder = DailyReminder()
        val notifPref = findPreference<SwitchPreference>(getString(R.string.pref_key_notify))

        notifPref?.setOnPreferenceChangeListener { _, newValue ->
            val newVal = newValue as Boolean

            if (newVal) {
                dailyReminder.setDailyReminder(requireContext())
            } else {
                dailyReminder.cancelAlarm(requireContext())
            }
            true
        }
    }

    private fun updateTheme(nightMode: Int): Boolean {
        AppCompatDelegate.setDefaultNightMode(nightMode)
        requireActivity().recreate()
        return true
    }
}