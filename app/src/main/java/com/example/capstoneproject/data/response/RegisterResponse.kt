package com.example.capstoneproject.data.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("data")
	val registerData: RegisterData? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class RegisterData(

	@field:SerializedName("firstname")
	val firstname: String? = null,

	@field:SerializedName("mobile")
	val mobile: Any? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("lastname")
	val lastname: String? = null
)
