package com.example.capstoneproject.ui.login

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
    val userDummy: LiveData<List<DataUser>> = _userDummy
    val loginUser = MutableLiveData<DataUser?>()
    /*val userDummy = mutableListOf<dataUser>()
    val loginUser = MutableLiveData<dataUser?>()*/

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
        loginUser.value = userLogin

        if (loginUser.value != null) {
            viewModelScope.launch {
                preferences.saveFullname("${loginUser.value!!.firstname} ${loginUser.value!!.lastname}")
                preferences.saveEmail(loginUser.value!!.email.toString())
                preferences.savePhone(loginUser.value!!.mobile.toString())
            }
        }
    }
}