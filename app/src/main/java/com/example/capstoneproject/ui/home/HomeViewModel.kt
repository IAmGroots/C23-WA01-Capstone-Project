package com.example.capstoneproject.ui.home

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.capstoneproject.data.di.Injection
import com.example.capstoneproject.data.repository.MidtransRepository
import com.example.capstoneproject.model.Articles
import com.example.capstoneproject.model.DataSourceArticles
import com.example.capstoneproject.model.Package

class HomeViewModel : ViewModel() {

    private val midtransRepository = Injection.provideRepository()
    private val db = Injection.provideDatabaseFirestore()
    private val listService = mutableListOf<Package>()

    private val _listArticle = MutableLiveData<List<Articles>>()
    val listArticle: LiveData<List<Articles>> = _listArticle

    private val _isLastTransaction = MutableLiveData<String>()
    val lastTrasaction: LiveData<String> = _isLastTransaction

    init {
        getMoreArticles(4)
        getAllService()
    }

    fun getMoreArticles(total: Int) {
        _listArticle.value = DataSourceArticles.dataArticles.take(total)
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

    fun getService(idService: String): String {
        return listService.find { it.id == idService }?.name ?: "none"
    }

    fun checkStatusTransaction(idOrder: String) {
        midtransRepository.getStatusTransaction(idOrder) { response ->
            response?.let {
                _isLastTransaction.value = when (response.transactionStatus.toString()) {
                    "settlement" -> "Success"
                    "pending" -> "Pending"
                    else -> "Expired"
                }
                Log.d("HomeViewModel", "Something wrong")
            }
        }
    }
}