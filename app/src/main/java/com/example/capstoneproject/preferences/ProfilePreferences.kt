package com.example.capstoneproject.preferences

import androidx.datastore.preferences.core.stringPreferencesKey

class ProfilePreferences {
    companion object {
        val TOKEN_KEY = stringPreferencesKey("token")
        val UID_KEY = stringPreferencesKey("uid")
        val FIRST_NAME_KEY = stringPreferencesKey("firstname")
        val LAST_NAME_KEY = stringPreferencesKey("lastname")
        val EMAIL_KEY = stringPreferencesKey("email")
        val MOBILE_KEY = stringPreferencesKey("mobile")
        val STATE_KEY = stringPreferencesKey("state")
        val CITY_KEY = stringPreferencesKey("city")
        val ADDRESS_KEY = stringPreferencesKey("address")
    }
}