package com.example.capstoneproject.data.response

import com.google.gson.annotations.SerializedName

data class OneArticlesResponse(

	@field:SerializedName("data")
	val data: DataArticle? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataArticle(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("content")
	val content: String? = null
)
