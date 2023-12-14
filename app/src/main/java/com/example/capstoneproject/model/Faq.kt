package com.example.capstoneproject.model

data class Faq(
    val question: String,
    val answer: String,
    var isExpandable: Boolean = false
)
