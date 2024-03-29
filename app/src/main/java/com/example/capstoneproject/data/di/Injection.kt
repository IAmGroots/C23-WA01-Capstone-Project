package com.example.capstoneproject.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.capstoneproject.data.repository.MidtransRepository
import com.example.capstoneproject.data.repository.UserRepository
import com.example.capstoneproject.data.retrofit.ApiConfig
import com.example.capstoneproject.preferences.dataStore
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

object Injection {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    fun provideRepository(): MidtransRepository {
        val apiService= ApiConfig.getApiService()
        return MidtransRepository(apiService)
    }

    fun provideDatabaseFirestore(): FirebaseFirestore {
        return Firebase.firestore
    }

    fun provideUserRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(context.dataStore, apiService)
    }
}