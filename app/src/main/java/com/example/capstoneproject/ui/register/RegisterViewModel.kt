package com.example.capstoneproject.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.data.repository.UserRepository
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setLoading(condition: Boolean) {
        _isLoading.value = condition
    }

    fun register(firstname: String, lastname: String, email: String, password: String, confirmPassword: String) =
        repository.register(firstname, lastname, email, password, confirmPassword)

    fun saveUID(uid: String) {
        viewModelScope.launch {
            repository.saveUID(uid)
        }
    }
}