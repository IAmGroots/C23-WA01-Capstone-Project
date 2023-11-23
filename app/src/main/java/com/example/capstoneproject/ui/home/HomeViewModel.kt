package com.example.capstoneproject.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.capstoneproject.model.Articles
import com.example.capstoneproject.model.DataSourceArticles

class HomeViewModel : ViewModel() {

    private val _listArticle = MutableLiveData<List<Articles>>()
    val listArticle: LiveData<List<Articles>> = _listArticle

    init {
        getMoreArticles(4)
    }

    fun getMoreArticles(total: Int) {
        _listArticle.value = DataSourceArticles.dataArticles.take(total)
    }
}