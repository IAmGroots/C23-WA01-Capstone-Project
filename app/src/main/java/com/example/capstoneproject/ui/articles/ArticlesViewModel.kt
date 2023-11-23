package com.example.capstoneproject.ui.articles

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.capstoneproject.model.Articles
import com.example.capstoneproject.model.DataSourceArticles

class ArticlesViewModel : ViewModel() {

    private val _listArticle = MutableLiveData<List<Articles>>()
    val listArticle: LiveData<List<Articles>> = _listArticle

    init {
        getAllArticles()
    }

    private fun getAllArticles() {
        _listArticle.value = DataSourceArticles.dataArticles
    }

}