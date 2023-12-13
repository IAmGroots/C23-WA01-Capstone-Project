package com.example.capstoneproject.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.preferences.SettingPreferences
import kotlinx.coroutines.launch

class LoginViewModel(private val preferences: SettingPreferences) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setLoading(condition: Boolean) {
        _isLoading.value = condition
    }

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