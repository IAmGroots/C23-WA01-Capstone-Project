package com.example.capstoneproject.data.response

import com.google.gson.annotations.SerializedName

data class OtpResponse(

	@field:SerializedName("data")
	val data: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
