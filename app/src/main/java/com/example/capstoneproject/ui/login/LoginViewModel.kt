package com.example.capstoneproject.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.data.repository.UserRepository
import com.example.capstoneproject.data.response.Profile
import com.example.capstoneproject.preferences.SettingPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setLoading(condition: Boolean) {
        _isLoading.value = condition
    }

    fun getBiometric(): LiveData<Boolean> = repository.getBiometricSetting().asLiveData()

    fun saveLogin(isLogin: Boolean) {
        viewModelScope.launch {
            repository.saveLogin(isLogin)
        }
    }

    fun saveToken(token: String) {
        viewModelScope.launch {
            repository.saveToken(token)
        }
    }

    fun saveUID(uid: String) {
        viewModelScope.launch {
            repository.saveUID(uid)
        }
    }

    fun saveFirstname(firstname: String) {
        viewModelScope.launch {
            repository.saveFirstname(firstname)
        }
    }

    fun saveLastname(lastname: String) {
        viewModelScope.launch {
            repository.saveLastname(lastname)
        }
    }

    fun saveEmail(email: String) {
        viewModelScope.launch {
            repository.saveEmail(email)
        }
    }

//    fun getProfile(): LiveData<Profile?> {
//        return repository.getProfile().asLiveData()
//    }

    fun login(email: String, password: String) = repository.login(email, password)
}