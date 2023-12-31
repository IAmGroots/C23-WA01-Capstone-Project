package com.example.capstoneproject.data.repository

import com.example.capstoneproject.data.response.CancelTransactionResponse
import com.example.capstoneproject.data.response.StatusTransactionResponse
import com.example.capstoneproject.data.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MidtransRepository(
    private val apiService: ApiService
) {
    fun getStatusTransaction(orderId: String, onResult: (StatusTransactionResponse?) -> Unit) {
        apiService.getStatusTransaction(orderId).enqueue(object :
            Callback<StatusTransactionResponse> {
            override fun onResponse(
                call: Call<StatusTransactionResponse>,
                response: Response<StatusTransactionResponse>
            ) {
                if (response.isSuccessful) {
                    val statusTransaction = response.body()
                    onResult(statusTransaction)
                } else {
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<StatusTransactionResponse>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun cancelTransaction(orderId: String, onResult: (CancelTransactionResponse?) -> Unit) {
        apiService.cancelTransaction(orderId).enqueue(object :
            Callback<CancelTransactionResponse> {
            override fun onResponse(
                call: Call<CancelTransactionResponse>,
                response: Response<CancelTransactionResponse>
            ) {
                if (response.isSuccessful) {
                    val statusTransaction = response.body()
                    onResult(statusTransaction)
                } else {
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<CancelTransactionResponse>, t: Throwable) {
                onResult(null)
            }
        })
    }
}