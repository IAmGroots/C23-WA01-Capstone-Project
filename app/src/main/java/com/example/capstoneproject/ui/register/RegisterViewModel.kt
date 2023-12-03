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

    fun checkEmail(email: String) {
        val check = _userDummy.value?.find { it.email == email }
        userEmail.value = check
    }

    fun checkMobile(mobile: String) {
        val check = _userDummy.value?.find { it.mobile == mobile }
        userMobile.value = check
    }

}