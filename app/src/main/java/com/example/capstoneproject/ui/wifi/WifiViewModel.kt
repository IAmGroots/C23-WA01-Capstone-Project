package com.example.capstoneproject.ui.wifi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.capstoneproject.model.Articles
import com.example.capstoneproject.model.DataSourceArticles
import com.example.capstoneproject.model.DataSourceHotspot
import com.example.capstoneproject.model.Hotspot

class WifiViewModel : ViewModel() {
    private val _listHotspot = MutableLiveData<List<Hotspot>>()
    val listHotspot: LiveData<List<Hotspot>> = _listHotspot

    init {
        getAllHotspot()
    }

    private fun getAllHotspot() {
        _listHotspot.value = DataSourceHotspot.dataHotspot
    }
}