package com.example.capstoneproject.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.preferences.SettingPreferences
import kotlinx.coroutines.launch

class LoginViewModel(private val preferences: SettingPreferences) : ViewModel() {

    fun getBiometric(): LiveData<Boolean> = preferences.getBiometricSetting().asLiveData()
    fun saveLogin(isLogin: Boolean) {
        viewModelScope.launch {
            preferences.saveLogin(isLogin)
        }
    }

    fun saveUID(uid: String) {
        viewModelScope.launch {
            preferences.saveUID(uid)
        }
    }

}