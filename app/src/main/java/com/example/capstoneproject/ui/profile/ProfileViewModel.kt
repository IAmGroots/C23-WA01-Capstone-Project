package com.example.capstoneproject.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.model.DataSourceHistoryPayment
import com.example.capstoneproject.model.HistoryPayment
import com.example.capstoneproject.preferences.SettingPreferences
import kotlinx.coroutines.launch

class ProfileViewModel(private val preferences: SettingPreferences) : ViewModel() {
    private val _listHistoryPayment = MutableLiveData<List<HistoryPayment>>()
    val listHistoryPayment: LiveData<List<HistoryPayment>> = _listHistoryPayment

    init {
        getAllHistoryPayment()
    }

    private fun getAllHistoryPayment() {
        _listHistoryPayment.value = DataSourceHistoryPayment.dataHistoryPayment
    }

    fun getHasBiometric(): LiveData<Boolean> = preferences.getHasBiometric().asLiveData()
    fun getBiometric(): LiveData<Boolean> = preferences.getBiometricSetting().asLiveData()

    fun getTheme(): LiveData<Boolean> = preferences.getThemeSetting().asLiveData()


    fun saveBiometric(isBiometricActive : Boolean) {
        viewModelScope.launch {
            preferences.saveBiometricSetting(isBiometricActive)
        }
    }

    fun saveTheme(isDarkModeActive : Boolean) {
        viewModelScope.launch {
            preferences.saveThemeSetting(isDarkModeActive)
        }
    }
}