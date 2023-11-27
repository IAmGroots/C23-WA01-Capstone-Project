package com.example.capstoneproject.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.capstoneproject.model.DataSourceArticles
import com.example.capstoneproject.model.dataSorceUser
import com.example.capstoneproject.model.dataUser

class LoginViewModel : ViewModel() {

    val _userDummy = MutableLiveData<List<dataUser>>()

    private val _loggedInUser = MutableLiveData<dataUser?>()
    val loggedInUser: LiveData<dataUser?> = _loggedInUser

    init {
        getLogin()
    }

    private fun getLogin() {
        _userDummy.value = dataSorceUser.user
    }

    fun login(email: String, password: String) {
        val userLogin = _userDummy.value?.find {
            it.email == email && it.password == password
        }
        _loggedInUser.value = userLogin

        userLogin?.let {
            Log.e("LoginViewModel", "User logged in: ${it.firstname}, ${it.lastname}, ${it.email}")
        }
    }
}