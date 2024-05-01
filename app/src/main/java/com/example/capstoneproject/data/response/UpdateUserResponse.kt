package com.example.capstoneproject.data.response

import com.google.gson.annotations.SerializedName

data class UpdateUserResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Data(

	@field:SerializedName("user_status")
	val userStatus: String? = null,

	@field:SerializedName("firstname")
	val firstname: String? = null,

	@field:SerializedName("google_id")
	val googleId: Any? = null,

	@field:SerializedName("address2")
	val address2: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("address1")
	val address1: String? = null,

	@field:SerializedName("mobile")
	val mobile: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("uuid")
	val uuid: String? = null,

	@field:SerializedName("lastname")
	val lastname: String? = null,

	@field:SerializedName("home")
	val home: Any? = null,

	@field:SerializedName("country_code")
	val countryCode: String? = null,

	@field:SerializedName("whmcs_id")
	val whmcsId: Any? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("postalcode")
	val postalcode: String? = null,

	@field:SerializedName("salutation")
	val salutation: Any? = null,

	@field:SerializedName("state")
	val state: String? = null,

	@field:SerializedName("photo_url")
	val photoUrl: Any? = null,

	@field:SerializedName("radius_key")
	val radiusKey: Any? = null,

	@field:SerializedName("email")
	val email: String? = null
)
