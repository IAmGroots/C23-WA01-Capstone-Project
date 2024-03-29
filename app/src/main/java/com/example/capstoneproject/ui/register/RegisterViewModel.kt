package com.example.capstoneproject.ui.register

import androidx.lifecycle.ViewModel
import com.example.capstoneproject.data.repository.UserRepository

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {
    fun register(firstname: String, lastname: String, email: String, password: String, confirmPassword: String) =
        repository.register(firstname, lastname, email, password, confirmPassword)
}