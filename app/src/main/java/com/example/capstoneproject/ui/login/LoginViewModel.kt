package com.example.capstoneproject.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.capstoneproject.model.Articles
import com.example.capstoneproject.model.DataSourceArticles
import com.example.capstoneproject.model.dataSorceUser
import com.example.capstoneproject.model.dataUser

class LoginViewModel : ViewModel() {

    val _userDummy = MutableLiveData<List<dataUser>>()
    val userDummy: LiveData<List<dataUser>> = _userDummy
    val loginUser = MutableLiveData<dataUser?>()
    /*val userDummy = mutableListOf<dataUser>()
    val loginUser = MutableLiveData<dataUser?>()*/

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
        loginUser.value = userLogin
    }
}