package com.example.capstoneproject.data.retrofit

import com.example.capstoneproject.data.response.CancelTransactionResponse
import com.example.capstoneproject.data.response.StatusTransactionResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @GET("v2/{order_id}/status")
    fun getStatusTransaction(@Path("order_id") orderId: String): Call<StatusTransactionResponse>

    @POST("v2/{order_id}/cancel")
    fun cancelTransaction(@Path("order_id") orderId: String): Call<CancelTransactionResponse>

}