package com.example.capstoneproject.ui.profile.edit_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.data.repository.LocationRepository
import com.example.capstoneproject.data.repository.UserRepository
import com.example.capstoneproject.data.retrofit.ApiConfig
import com.example.capstoneproject.data.retrofit.ApiService
import com.example.capstoneproject.data.retrofit.ApiServiceLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditProfileViewModel(private val repository: UserRepository) : ViewModel() {

    private val locationRepository = LocationRepository.getInstance(ApiConfig.getApiServiceLocation())
    val provinceNames: MutableList<String> = mutableListOf()
    val cityNames: MutableList<String> = mutableListOf()
    val provinceDictionary: MutableMap<String, String> = mutableMapOf()
    fun updateUser(
        token: String,
        firstname: String,
        lastname: String,
        mobile: String,
        user_id: String,
        address1: String,
        city: String,
        state: String
    ) = repository.updateUser(token, firstname, lastname, mobile, user_id, address1, city, state)


    fun getProvinces() {
        viewModelScope.launch {
            try {
                val provinceResponse = withContext(Dispatchers.IO) {
                    locationRepository.getProvinces()
                }
                provinceResponse.data?.forEach { province ->
                    province?.code?.let { code ->
                        province?.name?.let { name ->
                            provinceDictionary[code] = name
                        }
                    }
                    province?.name?.let { provinceNames.add(it) }
//                    println("Province Code: ${province?.code}")
//                    println("Province Name: ${province?.name}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getCity(provinceCode: String) {
        viewModelScope.launch {
            try {
                val regencyResponse = withContext(Dispatchers.IO) {
                    locationRepository.getCity(provinceCode)
                }
                regencyResponse.data?.forEach { city ->
                    city?.name?.let { cityNames.add(it) }
//                    println("City Code: ${city?.code}")
//                    println("City Name: ${city?.name}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private val _isCityActive = MutableLiveData<Boolean>()
    val isCityActive: LiveData<Boolean> = _isCityActive

    fun setCity(condition: Boolean) {
        _isCityActive.value = condition
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setLoading(condition: Boolean) {
        _isLoading.value = condition
    }

    fun getToken(): LiveData<String> {
        return repository.getToken().asLiveData()
    }

    fun getUserId(): LiveData<String> {
        return repository.getUID().asLiveData()
    }

    fun getFirstname(): LiveData<String> {
        return repository.getFirstname().asLiveData()
    }

    fun getLastname(): LiveData<String> {
        return repository.getLastname().asLiveData()
    }

    fun getEmail(): LiveData<String> {
        return repository.getEmail().asLiveData()
    }

    fun saveFirstname(firstname: String) {
        viewModelScope.launch {
            repository.saveFirstname(firstname)
        }
    }

    fun saveLastname(lastname: String) {
        viewModelScope.launch {
            repository.saveLastname(lastname)
        }
    }

}