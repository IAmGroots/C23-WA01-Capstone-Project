package com.example.capstoneproject.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.data.repository.UserRepository
import com.example.capstoneproject.data.response.UserProfile
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    val userProfile: LiveData<UserProfile> = repository.getProfile().asLiveData()

    fun setLoading(condition: Boolean) {
        _isLoading.value = condition
    }

    fun getBiometric(): LiveData<Boolean> = repository.getBiometricSetting().asLiveData()

    fun saveProfile(profile: UserProfile) {
        viewModelScope.launch {
            repository.saveProfile(profile)
        }
    }

    fun saveLogin(isLogin: Boolean) = viewModelScope.launch {
        repository.saveLogin(isLogin)
    }

    fun login(email: String, password: String) = repository.login(email, password)
}