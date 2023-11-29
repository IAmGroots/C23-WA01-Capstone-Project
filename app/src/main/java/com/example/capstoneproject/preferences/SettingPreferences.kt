package com.example.capstoneproject.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

class SettingPreferences constructor(private val dataStore: DataStore<Preferences>) {

    private val BIOMETRIC_KEY = booleanPreferencesKey("biometric_setting")
    private val EMAIL_KEY = stringPreferencesKey("email_string")
    private val HAS_BIOMETRIC = booleanPreferencesKey("has_biometric")

    fun getEmail(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[EMAIL_KEY] ?: "empty"
        }
    }
    suspend fun saveEmail(email : String) {
        dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = email
        }
    }

    fun getBiometricSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[BIOMETRIC_KEY] ?: false
        }
    }
    suspend fun saveBiometricSetting(biometricActive : Boolean) {
        dataStore.edit { preferences ->
            preferences[BIOMETRIC_KEY] = biometricActive
        }
    }

    fun getHasBiometric(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[HAS_BIOMETRIC] ?: false
        }
    }
    suspend fun saveHasBiometric(hasBiometric : Boolean) {
        dataStore.edit { preferences ->
            preferences[HAS_BIOMETRIC] = hasBiometric
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SettingPreferences? = null
        fun getInstance(dataStore: DataStore<Preferences>): SettingPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
