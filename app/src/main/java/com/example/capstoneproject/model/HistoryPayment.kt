package com.example.capstoneproject.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HistoryPayment(
    val id: Long,
    val invoiceDate: String,
    val payment: Int
) : Parcelable
