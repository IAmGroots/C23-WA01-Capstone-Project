package com.example.capstoneproject.data.response

import com.google.gson.annotations.SerializedName

data class ProvinceResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("meta")
	val meta: Meta? = null
)

data class DataItem(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("coordinates")
	val coordinates: Coordinates? = null,

	@field:SerializedName("google_place_id")
	val googlePlaceId: String? = null
)

data class Meta(

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("administrative_area_level")
	val administrativeAreaLevel: Int? = null
)

data class Coordinates(

	@field:SerializedName("lng")
	val lng: String? = null,

	@field:SerializedName("lat")
	val lat: String? = null
)
