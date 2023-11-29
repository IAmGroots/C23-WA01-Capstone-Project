package com.example.capstoneproject.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.model.DataSourceUser
import com.example.capstoneproject.model.DataUser
import com.example.capstoneproject.preferences.SettingPreferences
import kotlinx.coroutines.launch

class LoginViewModel(private val preferences: SettingPreferences) : ViewModel() {

    val _userDummy = MutableLiveData<List<DataUser>>()

    private val _loggedInUser = MutableLiveData<DataUser?>()
    val loggedInUser: LiveData<DataUser?> = _loggedInUser

    fun getEmail(): LiveData<String> = preferences.getEmail().asLiveData()

    init {
        getLogin()
    }

    private fun getLogin() {
        _userDummy.value = DataSourceUser.user
    }

    fun login(email: String, password: String) {
        val userLogin = _userDummy.value?.find {
            it.email == email && it.password == password
        }
        _loggedInUser.value = userLogin

        if (loggedInUser.value != null) {
            viewModelScope.launch {
                preferences.saveEmail(loggedInUser.value!!.email.toString())
            }
        }

        userLogin?.let {
            Log.e("LoginViewModel", "User logged in: ${it.firstname}, ${it.lastname}, ${it.email}")
        }
    }

    fun saveHasBiometric(hasBiometric : Boolean) {
        viewModelScope.launch {
            preferences.saveHasBiometric(hasBiometric)
        }
    }
}