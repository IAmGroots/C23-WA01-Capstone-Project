package com.example.capstoneproject.data.response

import com.google.gson.annotations.SerializedName

data class ArticlesResponse(

	@field:SerializedName("data")
	val data: List<ArticleItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class ArticleItem(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("content")
	val content: String? = null
)
