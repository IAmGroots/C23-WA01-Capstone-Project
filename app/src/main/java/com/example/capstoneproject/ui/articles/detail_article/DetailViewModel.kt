package com.example.capstoneproject.ui.articles.detail_article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.data.response.OneArticlesResponse
import com.example.capstoneproject.data.repository.UserRepository
import com.example.capstoneproject.data.response.ArticlesResponse
import com.example.capstoneproject.data.response.UserProfile
import com.example.capstoneproject.data.result.Result
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: UserRepository) : ViewModel() {

    val userProfile: LiveData<UserProfile> = repository.getProfile().asLiveData()

    private val _articles = MutableLiveData<Result<OneArticlesResponse>>()
    val articles: LiveData<Result<OneArticlesResponse>> = _articles

    fun fetchArticles(id: String, token: String) {
        viewModelScope.launch {
            repository.getArticle(id, token).observeForever {
                _articles.postValue(it)
            }
        }
    }

    private val _listArticles = MutableLiveData<Result<ArticlesResponse>>()
    val listArticles: LiveData<Result<ArticlesResponse>> = _listArticles

    fun fetchListArticles(token: String) {
        viewModelScope.launch {
            repository.getListArticle(token).observeForever {
                _listArticles.postValue(it)
            }
        }
    }

}