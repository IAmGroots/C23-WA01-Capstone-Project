package com.example.capstoneproject.ui.profile.edit_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.capstoneproject.data.repository.UserRepository

class EditProfileViewModel(private val repository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setLoading(condition: Boolean) {
        _isLoading.value = condition
    }

    fun getFirstname(): LiveData<String> {
        return repository.getFirstname().asLiveData()
    }

    fun getLastname(): LiveData<String> {
        return repository.getLastname().asLiveData()
    }

    fun getEmail(): LiveData<String> {
        return repository.getEmail().asLiveData()
    }

}