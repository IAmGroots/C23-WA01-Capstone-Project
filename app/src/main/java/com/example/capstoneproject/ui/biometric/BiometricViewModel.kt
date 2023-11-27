package com.example.capstoneproject.ui.biometric

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.capstoneproject.preferences.SettingPreferences

class BiometricViewModel(private val preferences: SettingPreferences) : ViewModel() {
    fun getBiometric(): LiveData<Boolean> = preferences.getBiometricSetting().asLiveData()
}