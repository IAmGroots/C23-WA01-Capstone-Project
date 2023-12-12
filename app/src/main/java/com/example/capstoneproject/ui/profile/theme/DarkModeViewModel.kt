package com.example.capstoneproject.ui.profile.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.preferences.SettingPreferences
import kotlinx.coroutines.launch

class DarkModeViewModel(private val preferences: SettingPreferences) : ViewModel()  {

    fun getTheme(): LiveData<Boolean> = preferences.getThemeSetting().asLiveData()

    fun saveTheme(isDarkModeActive : Boolean) {
        viewModelScope.launch {
            preferences.saveThemeSetting(isDarkModeActive)
        }
    }
}