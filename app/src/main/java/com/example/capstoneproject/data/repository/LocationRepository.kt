package com.example.capstoneproject.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.capstoneproject.data.response.ProvinceResponse
import com.example.capstoneproject.data.response.RegencyResponse
import com.example.capstoneproject.data.retrofit.ApiService
import com.example.capstoneproject.data.retrofit.ApiServiceLocation

class LocationRepository private constructor(
    private val apiServiceLocation: ApiServiceLocation
) {
    suspend fun getProvinces(): ProvinceResponse {
        return apiServiceLocation.getProvinces()
    }

    suspend fun getCity(provinceCode: String): RegencyResponse {
        return apiServiceLocation.getRegencies(provinceCode)
    }

    companion object {
        @Volatile
        private var INSTANCE: LocationRepository? = null

        fun getInstance(
            apiServiceLocation: ApiServiceLocation
        ): LocationRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = LocationRepository(apiServiceLocation)
                INSTANCE = instance
                instance
            }
        }
    }
}