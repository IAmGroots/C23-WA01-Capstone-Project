package com.example.capstoneproject.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.capstoneproject.model.DataSourceHistoryPayment
import com.example.capstoneproject.model.HistoryPayment

class ProfileViewModel : ViewModel() {
    private val _listHistoryPayment = MutableLiveData<List<HistoryPayment>>()
    val listHistoryPayment: LiveData<List<HistoryPayment>> = _listHistoryPayment

    init {
        getAllHistoryPayment()
    }

    private fun getAllHistoryPayment() {
        _listHistoryPayment.value = DataSourceHistoryPayment.dataHistoryPayment
    }
}