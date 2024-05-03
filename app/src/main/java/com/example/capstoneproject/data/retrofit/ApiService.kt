package com.example.capstoneproject.data.retrofit

import com.example.capstoneproject.data.response.CancelTransactionResponse
import com.example.capstoneproject.data.response.ListWifiResponse
import com.example.capstoneproject.data.response.LoginResponse
import com.example.capstoneproject.data.response.OtpResponse
import com.example.capstoneproject.data.response.ProvinceResponse
import com.example.capstoneproject.data.response.RegencyResponse
import com.example.capstoneproject.data.response.RegisterResponse
import com.example.capstoneproject.data.response.StatusTransactionResponse
import com.example.capstoneproject.data.response.UpdateUserResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
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

    @FormUrlEncoded
    @POST("v1/update/user")
    suspend fun updateUser(
        @Header("Authorization") token: String,
        @Field("firstname") firstname: String,
        @Field("lastname") lastname: String,
        @Field("mobile") mobile: String,
        @Field("user_id") user_id: String,
        @Field("address1") address1: String,
        @Field("city") city: String,
        @Field("state") state: String
    ): UpdateUserResponse

    @GET("v1/wifi/list")
    suspend fun getListWifi(
        @Header("Authorization") token: String
    ): ListWifiResponse

    @FormUrlEncoded
    @POST("validate/otp")
    suspend fun validateOtp(
        @Field("uuid") city: String,
        @Field("otp") state: String
    ): OtpResponse
}

interface ApiServiceLocation {
    @GET("provinces.json")
    suspend fun getProvinces(): ProvinceResponse

    @GET("regencies/{provinceCode}.json")
    suspend fun getRegencies(@Path("provinceCode") provinceCode: String): RegencyResponse
}