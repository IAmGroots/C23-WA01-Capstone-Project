package com.example.capstoneproject.model

import com.example.capstoneproject.data.response.StatusTransactionResponse

data class History (
    val idOrder: String,
    val idUser: String,
    val idService: Package,
    val statusTransaction: StatusTransactionResponse
)