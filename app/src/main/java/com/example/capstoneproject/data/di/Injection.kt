package com.example.capstoneproject.data.di

import com.example.capstoneproject.data.repository.MidtransRepository
import com.example.capstoneproject.data.retrofit.ApiConfig
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

object Injection {
    fun provideRepository(): MidtransRepository {
        val apiService= ApiConfig.getApiService()
        return MidtransRepository(apiService)
    }

    fun provideDatabaseFirestore(): FirebaseFirestore {
        return Firebase.firestore
    }
}