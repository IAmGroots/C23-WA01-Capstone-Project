package com.example.capstoneproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.capstoneproject.preferences.SettingPreferences

class MainViewModel(private val preferences: SettingPreferences) : ViewModel() {

    fun getTheme(): LiveData<Boolean> = preferences.getThemeSetting().asLiveData()
    fun getBiometric(): LiveData<Boolean> = preferences.getBiometricSetting().asLiveData()
    fun getLogin(): LiveData<Boolean> = preferences.getLogin().asLiveData()

}