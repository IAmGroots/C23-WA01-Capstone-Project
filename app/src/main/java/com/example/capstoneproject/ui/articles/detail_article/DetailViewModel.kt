package com.example.capstoneproject.ui.articles.detail_article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.capstoneproject.model.Articles
import com.example.capstoneproject.model.DataSourceArticles

class DetailViewModel : ViewModel() {

    private val _listArticle = MutableLiveData<List<Articles>>()
    val listArticle: LiveData<List<Articles>> = _listArticle

    fun getMoreArticles(id: Long) {
        _listArticle.value = DataSourceArticles.dataArticles.filter { it.id != id }
    }

}