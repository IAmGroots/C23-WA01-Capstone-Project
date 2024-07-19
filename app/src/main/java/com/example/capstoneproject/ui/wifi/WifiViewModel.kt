package com.example.capstoneproject.ui.wifi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.capstoneproject.data.repository.UserRepository
import com.example.capstoneproject.data.response.UserProfile
import com.example.capstoneproject.data.response.WifiLocation
import com.example.capstoneproject.model.DataSourceHotspot
import com.example.capstoneproject.model.Hotspot

class WifiViewModel(private val repository: UserRepository) : ViewModel() {

    val userProfile: LiveData<UserProfile> = repository.getProfile().asLiveData()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setLoading(condition: Boolean) {
        _isLoading.value = condition
    }

    fun getListWifi(token: String) = repository.getListWifi(token)
}