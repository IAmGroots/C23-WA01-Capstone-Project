package com.example.capstoneproject.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.data.repository.UserRepository
import com.example.capstoneproject.data.response.UserProfile
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: UserRepository) : ViewModel() {

    val userProfile: LiveData<UserProfile> = repository.getProfile().asLiveData()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setLoading(condition: Boolean) {
        _isLoading.value = condition
    }

    fun getTheme(): LiveData<Boolean> = repository.getThemeSetting().asLiveData()
    fun saveTheme(isDarkModeActive: Boolean) = viewModelScope.launch {
        repository.saveThemeSetting(isDarkModeActive)
    }

    fun getBiometric(): LiveData<Boolean> = repository.getBiometricSetting().asLiveData()
    fun saveBiometric(isBiometricActive: Boolean) = viewModelScope.launch {
        repository.saveBiometricSetting(isBiometricActive)
    }

    fun logout() {
            viewModelScope.launch {
                repository.clearProfile()
            }
    }
}