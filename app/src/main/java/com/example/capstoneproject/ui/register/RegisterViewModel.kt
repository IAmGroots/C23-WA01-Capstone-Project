package com.example.capstoneproject.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.capstoneproject.model.dataUser

class RegisterViewModel : ViewModel() {

    /*val userDummy = mutableListOf<dataUser>()
    val registUser = MutableLiveData<dataUser?>()*/
    /*val _userDummy = MutableLiveData<List<dataUser>>()
    val userDummy: LiveData<List<dataUser>> = _userDummy
    val registUser = MutableLiveData<dataUser?>()*/
    val _userDummy = MutableLiveData<MutableList<dataUser>>()
    val userDummy: MutableLiveData<MutableList<dataUser>> = _userDummy
    val registUser = MutableLiveData<dataUser?>()

    init {
        setUser()
    }

    private fun setUser() {
        _userDummy.value = mutableListOf()
    }

    fun regist(firstname: String, mobile: String, email: String, lastname: String, password: String) {
        if (_userDummy.value?.any { it.email == email } == true) {
            registUser.value = null
        } else {
            val newUser = dataUser(firstname = firstname, mobile = mobile,  id = _userDummy.value?.size ?: 1, email = email, lastname = lastname, password = password)
            _userDummy.value?.add(newUser)
            registUser.value = newUser
        }
        /*val newUser = dataUser(firstname = firstname, mobile = mobile,  id = userDummy.size + 1, email = email, lastname = lastname, password = password)
        _userDummy.value?.add(newUser)
        registUser.value = newUser*/
    }
}