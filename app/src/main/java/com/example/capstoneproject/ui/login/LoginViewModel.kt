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
// <<<<<<< Hadi
//         loginUser.value = userLogin

//         if (loginUser.value != null) {
//             viewModelScope.launch {
//                 preferences.saveFullname("${loginUser.value!!.firstname} ${loginUser.value!!.lastname}")
//                 preferences.saveEmail(loginUser.value!!.email.toString())
//                 preferences.savePhone(loginUser.value!!.mobile.toString())
//             }
// =======
        _loggedInUser.value = userLogin

        userLogin?.let {
            Log.e("LoginViewModel", "User logged in: ${it.firstname}, ${it.lastname}, ${it.email}")
// >>>>>>> main
        }
    }
}