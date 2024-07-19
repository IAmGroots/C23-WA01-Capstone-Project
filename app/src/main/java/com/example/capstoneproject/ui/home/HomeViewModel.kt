package com.example.capstoneproject.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.data.repository.UserRepository
import com.example.capstoneproject.data.response.ArticlesResponse
import com.example.capstoneproject.data.response.UserProfile
import com.example.capstoneproject.data.result.Result
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setLoading(condition: Boolean) {
        _isLoading.value = condition
    }

    val userProfile: LiveData<UserProfile> = repository.getProfile().asLiveData()

    private val _listArticles = MutableLiveData<Result<ArticlesResponse>>()
    val listArticles: LiveData<Result<ArticlesResponse>> = _listArticles

    fun fetchArticles(token: String) {
        viewModelScope.launch {
            repository.getListArticle(token).observeForever {
                _listArticles.postValue(it)
            }
        }
    }
}