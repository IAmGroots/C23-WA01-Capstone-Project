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

class HomeViewModel : ViewModel() {

    private val midtransRepository = Injection.provideRepository()

    private val _listArticle = MutableLiveData<List<Articles>>()
    val listArticle: LiveData<List<Articles>> = _listArticle

    private val _isLastTransaction = MutableLiveData<String>()
    val lastTrasaction: LiveData<String> = _isLastTransaction

    init {
        getMoreArticles(4)
    }

    fun getMoreArticles(total: Int) {
        _listArticle.value = DataSourceArticles.dataArticles.take(total)
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