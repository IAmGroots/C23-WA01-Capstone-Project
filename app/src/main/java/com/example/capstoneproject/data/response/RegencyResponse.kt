package com.example.capstoneproject.data.response

import com.google.gson.annotations.SerializedName

data class RegencyResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("meta")
	val meta: Meta? = null
)

