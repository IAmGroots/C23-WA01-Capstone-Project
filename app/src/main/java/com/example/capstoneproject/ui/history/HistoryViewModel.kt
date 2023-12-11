package com.example.capstoneproject.ui.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.capstoneproject.data.di.Injection
import com.example.capstoneproject.model.History
import com.example.capstoneproject.model.Order
import com.example.capstoneproject.model.Package

class HistoryViewModel : ViewModel() {

    val midtransRepository = Injection.provideRepository()
    private val db = Injection.provideDatabaseFirestore()

    private val _listHistory = MutableLiveData<List<History>>()
    val listHistory: LiveData<List<History>> = _listHistory

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val listService = mutableListOf<Package>()

    init {
        getAllService()
    }

    fun getAllService() {
        db.collection("services")
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val service = Package(
                        document.get("id").toString(),
                        document.get("name").toString(),
                        document.get("speed").toString().toInt(),
                        document.get("period").toString().toInt(),
                        document.get("price").toString().toInt(),
                    )
                    listService.add(service)
                }
            }
            .addOnFailureListener {
                Log.e("MainViewModel", "Error: ${it.message}", it)
            }
    }

    fun getService(idService: Int): Package? {
        return listService.find { it.id == idService.toString() }
    }

    fun getAllHistoryTransactionOnGoing(listOrder: List<Order>) {
        val resultList: MutableList<History> = mutableListOf()
        _isLoading.value = true
        Log.d("MainViewModel", "Sebelum For ViewModel")
        for (order in listOrder) {
            Log.d("MainViewModel", "Order : ${order.idOrder}")
            midtransRepository.getStatusTransaction(order.idOrder) { response ->
                Log.d("MainViewModel", "Sebelum Response ViewModel")
                response?.let {
                    if (response.transactionStatus == "pending") {
                        Log.d("MainViewModel", "Didalam Response ViewModel")
                        resultList.add(
                            History(
                                order.idOrder,
                                order.idUser,
                                order.service,
                                response
                            )
                        )
                        Log.d("MainViewModel", "Add Data ViewModel")
                    }
                }
                _listHistory.value = resultList
                Log.d("MainViewModel", "Set Data ViewModel")
                Log.d("MainViewModel", "Diluar Response ViewModel")
            }
        }
        _isLoading.value = false
    }

    fun getAllHistoryTransactionSuccessful(listOrder: List<Order>) {
        val resultList: MutableList<History> = mutableListOf()
        _isLoading.value = true
        Log.d("MainViewModel", "Sebelum For ViewModel")
        for (order in listOrder) {
            Log.d("MainViewModel", "Order : ${order.idOrder}")
            midtransRepository.getStatusTransaction(order.idOrder) { response ->
                Log.d("MainViewModel", "Sebelum Response ViewModel")
                response?.let {
                    if (response.transactionStatus == "settlement") {
                        Log.d("MainViewModel", "Didalam Response ViewModel")
                        resultList.add(
                            History(
                                order.idOrder,
                                order.idUser,
                                order.service,
                                response
                            )
                        )
                        Log.d("MainViewModel", "Add Data ViewModel")
                    }
                }
                _listHistory.value = resultList
                Log.d("MainViewModel", "Set Data ViewModel")
                Log.d("MainViewModel", "Diluar Response ViewModel")
            }
        }
        _isLoading.value = false
    }

    fun getAllHistoryTransactionUnsuccessful(listOrder: List<Order>) {
        val resultList: MutableList<History> = mutableListOf()
        _isLoading.value = true
        Log.d("MainViewModel", "Sebelum For ViewModel")
        for (order in listOrder) {
            Log.d("MainViewModel", "Order : ${order.idOrder}")
            midtransRepository.getStatusTransaction(order.idOrder) { response ->
                Log.d("MainViewModel", "Sebelum Response ViewModel")
                response?.let {
                    if (response.transactionStatus != "settlement" && response.transactionStatus != "pending") {
                        Log.d("MainViewModel", "Didalam Response ViewModel")
                        resultList.add(
                            History(
                                order.idOrder,
                                order.idUser,
                                order.service,
                                response
                            )
                        )
                        Log.d("MainViewModel", "Add Data ViewModel")
                    }
                }
                _listHistory.value = resultList
                Log.d("MainViewModel", "Set Data ViewModel")
                Log.d("MainViewModel", "Diluar Response ViewModel")
            }
        }
        _isLoading.value = false
    }
}