package com.example.capstoneproject.model

import androidx.room.PrimaryKey

data class dataRegistrasi(
    val firstname: String? = null,
    val mobile: String? = null,
    val id: Any? = null,
    @PrimaryKey
    val email: String? = null,
    val lastname: String? = null,
    val password: String? = null
)