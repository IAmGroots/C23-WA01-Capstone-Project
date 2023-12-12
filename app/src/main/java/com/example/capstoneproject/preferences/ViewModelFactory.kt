package com.example.capstoneproject.preferences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.capstoneproject.MainViewModel
import com.example.capstoneproject.ui.login.LoginViewModel
import com.example.capstoneproject.ui.profile.ProfileViewModel
import com.example.capstoneproject.ui.profile.biometric.BiometricViewModel
import com.example.capstoneproject.ui.profile.theme.DarkModeViewModel

class ViewModelFactory(private val preferences: SettingPreferences) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(preferences) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(preferences) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(preferences) as T
            }
            modelClass.isAssignableFrom(DarkModeViewModel::class.java) -> {
                DarkModeViewModel(preferences) as T
            }
            modelClass.isAssignableFrom(BiometricViewModel::class.java) -> {
                BiometricViewModel(preferences) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}