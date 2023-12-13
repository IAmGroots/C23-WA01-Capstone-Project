package com.example.capstoneproject.ui.history

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
    }

    fun getService(idService: Int): Package? {
        return listService.find { it.id == idService.toString() }
    }

    fun getAllHistoryTransactionOnGoing(listOrder: List<Order>, callback: () -> Unit) {
        val resultList: MutableList<History> = mutableListOf()
        for (order in listOrder) {
            midtransRepository.getStatusTransaction(order.idOrder) { response ->
                response?.let {
                    if (response.transactionStatus == "pending") {
                        resultList.add(
                            History(
                                order.idOrder,
                                order.idUser,
                                order.service,
                                response
                            )
                        )
                    }
                }
                _listHistory.value = resultList
                callback.invoke()
            }
        }
    }

    fun getAllHistoryTransactionSuccessful(listOrder: List<Order>, callback: () -> Unit) {
        val resultList: MutableList<History> = mutableListOf()
        for (order in listOrder) {
            midtransRepository.getStatusTransaction(order.idOrder) { response ->
                response?.let {
                    if (response.transactionStatus == "settlement") {
                        resultList.add(
                            History(
                                order.idOrder,
                                order.idUser,
                                order.service,
                                response
                            )
                        )
                    }
                }
                _listHistory.value = resultList
                callback.invoke()
            }
        }
    }

    fun getAllHistoryTransactionUnsuccessful(listOrder: List<Order>, callback: () -> Unit) {
        val resultList: MutableList<History> = mutableListOf()
        for (order in listOrder) {
            midtransRepository.getStatusTransaction(order.idOrder) { response ->
                response?.let {
                    if (response.transactionStatus != "settlement" && response.transactionStatus != "pending") {
                        resultList.add(
                            History(
                                order.idOrder,
                                order.idUser,
                                order.service,
                                response
                            )
                        )
                    }
                }
                _listHistory.value = resultList
                callback.invoke()
            }
        }
    }
}