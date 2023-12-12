package com.example.capstoneproject.ui.profile.biometric

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.preferences.SettingPreferences
import kotlinx.coroutines.launch

class BiometricViewModel(private val preferences: SettingPreferences) : ViewModel() {

    fun getBiometric(): LiveData<Boolean> = preferences.getBiometricSetting().asLiveData()

    fun saveBiometric(isBiometricActive : Boolean) {
        viewModelScope.launch {
            preferences.saveBiometricSetting(isBiometricActive)
        }
    }
}