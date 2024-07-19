package com.example.capstoneproject.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("data")
	val loginData: LoginData? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Profile(

	@field:SerializedName("user_status")
	val userStatus: String? = null,

	@field:SerializedName("firstname")
	val firstname: String? = null,

	@field:SerializedName("google_id")
	val googleId: Any? = null,

	@field:SerializedName("address2")
	val address2: Any? = null,

	@field:SerializedName("city")
	val city: Any? = null,

	@field:SerializedName("address1")
	val address1: Any? = null,

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
	val countryCode: Any? = null,

	@field:SerializedName("whmcs_id")
	val whmcsId: Any? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("postalcode")
	val postalcode: Any? = null,

	@field:SerializedName("salutation")
	val salutation: Any? = null,

	@field:SerializedName("state")
	val state: Any? = null,

	@field:SerializedName("photo_url")
	val photoUrl: Any? = null,

	@field:SerializedName("radius_key")
	val radiusKey: Any? = null,

	@field:SerializedName("email")
	val email: String? = null
)

data class LoginData(

	@field:SerializedName("access_token")
	val accessToken: String? = null,

	@field:SerializedName("refresh_token")
	val refreshToken: String? = null,

	@field:SerializedName("profile")
	val profile: Profile? = null,

	@field:SerializedName("token_type")
	val tokenType: String? = null,

	@field:SerializedName("expires_in")
	val expiresIn: Int? = null
)

data class UserProfile(
	val token: String,
	val uid: String,
	val firstName: String,
	val lastName: String,
	val email: String,
	val mobile: String,
	val state: String,
	val city: String,
	val address: String
)