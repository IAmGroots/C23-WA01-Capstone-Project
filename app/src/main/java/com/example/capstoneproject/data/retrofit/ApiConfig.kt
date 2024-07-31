package com.example.capstoneproject.data.retrofit

import com.example.capstoneproject.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        fun getApiService(): ApiService {
            val authInterceptor = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.NONE)
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .build()
            val retrofit = Retrofit.Builder()
//              Define the Base URL for the API in local.properties.
//              Replace this with the appropriate API key for development or production environment.
//              Example: BASE_URL_API=https://...
                .baseUrl(BuildConfig.BASE_URL_API)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }

        fun getApiServiceLocation(): ApiServiceLocation {
            val BASE_URL = "https://wilayah.id/api/"
            val client = OkHttpClient.Builder().build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiServiceLocation::class.java)
        }
//        fun getApiService(): ApiService {
//            val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
//            val client = OkHttpClient.Builder()
//                .addInterceptor(loggingInterceptor)
//                .addInterceptor{ chain ->
//                    val request = chain.request().newBuilder()
//                        .addHeader("Authorization", BuildConfig.AUTH_KEY_MIDTRANS)
//                        .build()
//                    chain.proceed(request)
//                }
//                .build()
//            val retrofit = Retrofit.Builder()
//                .baseUrl(BuildConfig.BASE_URL_MIDTRANS)
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(client)
//                .build()
//
//            return retrofit.create(ApiService::class.java)
//        }
    }
}