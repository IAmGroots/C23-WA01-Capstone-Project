package com.example.capstoneproject.data.retrofit

import com.example.capstoneproject.data.response.CancelTransactionResponse
import com.example.capstoneproject.data.response.LoginResponse
import com.example.capstoneproject.data.response.RegisterResponse
import com.example.capstoneproject.data.response.StatusTransactionResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @GET("v2/{order_id}/status")
    fun getStatusTransaction(@Path("order_id") orderId: String): Call<StatusTransactionResponse>

    @POST("v2/{order_id}/cancel")
    fun cancelTransaction(@Path("order_id") orderId: String): Call<CancelTransactionResponse>

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("firstname") firstname: String,
        @Field("lastname") lastname: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("password_confirmation") confirmPassword: String
    ): RegisterResponse
}