package com.example.capstoneproject.data.response

import com.google.gson.annotations.SerializedName

data class ListWifiResponse(
	val status: String,
	val message: String,
	val data: List<Map<String, WifiLocation>>
)

//data class Wifi(val wifiLocations: Map<String, WifiLocation>)

data class WifiLocation(
	val ssid: String,
	val name: String,
	val description: String,
	val city: String,
	val country: String,
	val coordinate: String
)

