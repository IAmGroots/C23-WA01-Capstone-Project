package com.example.capstoneproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.capstoneproject.data.repository.UserRepository
import com.example.capstoneproject.preferences.SettingPreferences

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    fun getTheme(): LiveData<Boolean> = repository.getThemeSetting().asLiveData()
    fun getBiometric(): LiveData<Boolean> = repository.getBiometricSetting().asLiveData()
    fun getLogin(): LiveData<Boolean> = repository.getLogin().asLiveData()

}