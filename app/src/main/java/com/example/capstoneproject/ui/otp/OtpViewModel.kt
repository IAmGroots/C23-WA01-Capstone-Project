package com.example.capstoneproject.ui.otp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.capstoneproject.data.repository.UserRepository

class OtpViewModel(private val repository: UserRepository): ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setLoading(condition: Boolean) {
        _isLoading.value = condition
    }

    fun validateOtp(uuid: String, otp: String) = repository.validateOtp(uuid, otp)

    fun getUUID(): LiveData<String> {
        return repository.getUID().asLiveData()
    }
}