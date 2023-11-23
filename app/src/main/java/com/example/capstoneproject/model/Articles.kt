package com.example.capstoneproject.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Articles (
    val id: Long,
    val url: String,
    val image: String,
    val title: String,
    val publishDate: String,
    val content: String
) : Parcelable