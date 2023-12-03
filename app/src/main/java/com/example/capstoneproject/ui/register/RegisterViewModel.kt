package com.example.capstoneproject.ui.register

import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.capstoneproject.model.DataSourceUser
import com.example.capstoneproject.model.DataUser
import com.example.capstoneproject.preferences.SettingPreferences

class RegisterViewModel() : ViewModel() {

    val _userDummy = MutableLiveData<MutableList<DataUser>>()
    val userEmail = MutableLiveData<DataUser?>()
    val userMobile = MutableLiveData<DataUser?>()

    init {
        setUser()
        loadInitialData()
    }

    private fun setUser() {
        _userDummy.value = mutableListOf()
    }

    private fun loadInitialData() {
        val initialUsers = DataSourceUser.user.toMutableList()
        _userDummy.value?.addAll(initialUsers)
    }

    fun regist(
        firstname: String,
        mobile: String,
        email: String,
        lastname: String,
        password: String,
        plan: String
    ) {
        val existingUser = _userDummy.value?.find { it.email == email }

        if (existingUser != null) {
            registUser.value = null
        } else {
            val newUser = DataUser(
                firstname = firstname,
                mobile = mobile,
                id = _userDummy.value?.size?.plus(1) ?: 1,
                email = email,
                lastname = lastname,
                password = password,
                plan = plan
            )

            val updatedUserList = _userDummy.value.orEmpty().toMutableList().apply {
                add(newUser)
                registUser.value = newUser
            }

            DataSourceUser.user = updatedUserList.toList()

            _userDummy.value?.let { users ->
                for (user in users) {
                    Log.e(
                        "UserData",
                        "ID: ${user.id}, Firstname: ${user.firstname}, Lastname: ${user.lastname}, Email: ${user.email}, Mobile: ${user.mobile}, Password: ${user.password}, Plan: ${user.plan}"
                    )
                }
            }
        }

    fun checkEmail(email: String) {
        val check = _userDummy.value?.find { it.email == email }
        userEmail.value = check
    }

    fun checkMobile(mobile: String) {
        val check = _userDummy.value?.find { it.mobile == mobile }
        userMobile.value = check
    }

}