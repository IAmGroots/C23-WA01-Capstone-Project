package com.example.capstoneproject.ui.profile.edit_profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.data.repository.LocationRepository
import com.example.capstoneproject.data.repository.UserRepository
import com.example.capstoneproject.data.response.UpdateUserResponse
import com.example.capstoneproject.data.response.UserProfile
import com.example.capstoneproject.data.result.Result
import com.example.capstoneproject.data.retrofit.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditProfileViewModel(private val repository: UserRepository) : ViewModel() {

    private val locationRepository = LocationRepository.getInstance(ApiConfig.getApiServiceLocation())
    val provinceNames: MutableList<String> = mutableListOf()
    val cityNames: MutableList<String> = mutableListOf()
    val provinceDictionary: MutableMap<String, String> = mutableMapOf()
    val userProfile: LiveData<UserProfile> = repository.getProfile().asLiveData()

    fun updateUser(
        token: String,
        firstname: String,
        lastname: String,
        mobile: String,
        address1: String,
        city: String,
        state: String
    ): LiveData<Result<UpdateUserResponse>> {
        return repository.updateUser(token, firstname, lastname, mobile, address1, city, state)
    }

    fun getProvinces() {
        viewModelScope.launch {
            when (val result = withContext(Dispatchers.IO) { locationRepository.getProvinces() }) {
                is Result.Success -> {
//                    setLoading(false)
                    result.data.data?.forEach { province ->
                        province?.code?.let { code ->
                            province?.name?.let { name ->
                                provinceDictionary[code] = name
                            }
                        }
                        province?.name?.let { provinceNames.add(it) }
                        Log.d("profile", "Province Code: ${province?.code}")
                        Log.d("profile", "Province Name: ${province?.name}")
                    }
                }
                is Result.Error -> {
//                    setLoading(false)
                    Log.e("profile", "Error fetching provinces: ${result.error}")
                }
                Result.Loading -> {
//                    setLoading(true)
                }
            }
        }
    }

    fun getCity(provinceCode: String) {
        viewModelScope.launch {
            when (val result = withContext(Dispatchers.IO) { locationRepository.getCity(provinceCode) }) {
                is Result.Success -> {
//                    setLoading(false)
                    result.data.data?.forEach { city ->
                        city?.name?.let { cityNames.add(it) }
                        Log.d("profile", "City Code: ${city?.code}")
                        Log.d("profile", "City Name: ${city?.name}")
                    }
                }
                is Result.Error -> {
//                    setLoading(false)
                    Log.e("profile", "Error fetching cities: ${result.error}")
                }
                Result.Loading -> {
//                    setLoading(true)
                }
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

    fun saveProfile(profile: UserProfile) {
        viewModelScope.launch {
            repository.saveProfile(profile)
        }
    }
}