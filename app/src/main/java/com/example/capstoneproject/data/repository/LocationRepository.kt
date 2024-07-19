package com.example.capstoneproject.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.capstoneproject.data.response.ProvinceResponse
import com.example.capstoneproject.data.response.RegencyResponse
import com.example.capstoneproject.data.result.Result
import com.example.capstoneproject.data.retrofit.ApiService
import com.example.capstoneproject.data.retrofit.ApiServiceLocation

class LocationRepository private constructor(
    private val apiServiceLocation: ApiServiceLocation
) {
    suspend fun getProvinces(): Result<ProvinceResponse> {
        return try {
            val response = apiServiceLocation.getProvinces()
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(e.localizedMessage ?: "Unknown error")
        }
    }

    suspend fun getCity(provinceCode: String): Result<RegencyResponse> {
        return try {
            val response = apiServiceLocation.getRegencies(provinceCode)
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(e.localizedMessage ?: "Unknown error")
        }
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