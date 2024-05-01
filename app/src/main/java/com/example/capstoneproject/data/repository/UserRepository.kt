package com.example.capstoneproject.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.capstoneproject.data.response.ListWifiResponse
import com.example.capstoneproject.data.response.LoginResponse
import com.example.capstoneproject.data.response.Profile
import com.example.capstoneproject.data.response.RegisterResponse
import com.example.capstoneproject.data.response.UpdateUserResponse
import com.example.capstoneproject.data.retrofit.ApiService
import com.example.capstoneproject.data.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.http.Field

class UserRepository private constructor(
    private val dataStore: DataStore<Preferences>,
    private val apiService: ApiService
) {

    fun login(
        email: String,
        password: String
    ): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService.login(email, password)
            emit(Result.Success(result))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }

    }

    fun register(
        firstname: String,
        lastname: String,
        email: String,
        password: String,
        confirmPassword: String
    ): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService.register(firstname, lastname, email, password, confirmPassword)
            emit(Result.Success(result))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

    fun updateUser(
        token: String,
        firstname: String,
        lastname: String,
        mobile: String,
        user_id: String,
        address1: String,
        city: String,
        state: String
    ): LiveData<Result<UpdateUserResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService.updateUser(token, firstname, lastname, mobile, user_id, address1, city, state)
            emit(Result.Success(result))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getListWifi(token: String): LiveData<Result<ListWifiResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService.getListWifi(token)
            emit(Result.Success(result))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun saveLogin(isLogin : Boolean) {
        dataStore.edit { preferences ->
            preferences[LOGIN_KEY] = isLogin
        }
    }

    suspend fun saveToken(token : String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    suspend fun saveUID(uid : String) {
        dataStore.edit { preferences ->
            preferences[UID_KEY] = uid
        }
    }

    suspend fun saveFirstname(firstname: String) {
        dataStore.edit { preferences ->
            preferences[FIRST_NAME_KEY] = firstname
        }
    }

    suspend fun saveLastname(lastname: String) {
        dataStore.edit { preferences ->
            preferences[LAST_NAME_KEY] = lastname
        }
    }

    suspend fun saveEmail(email: String) {
        dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = email
        }
    }

    fun getLogin(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[LOGIN_KEY] ?: false
        }
    }

    fun getToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN_KEY] ?: ""
        }
    }

    fun getUID(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[UID_KEY] ?: ""
        }
    }

    fun getFirstname(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[FIRST_NAME_KEY] ?: ""
        }
    }

    fun getLastname(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[LAST_NAME_KEY] ?: ""
        }
    }

    fun getEmail(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[EMAIL_KEY] ?: ""
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[LOGIN_KEY] = false
            preferences[TOKEN_KEY] = ""
            preferences[UID_KEY] = ""
            preferences[FIRST_NAME_KEY] = ""
            preferences[LAST_NAME_KEY] = ""
            preferences[EMAIL_KEY] = ""
            preferences[BIOMETRIC_KEY] = false
        }
    }

    suspend fun saveBiometricSetting(isBiometricActive : Boolean) {
        dataStore.edit { preferences ->
            preferences[BIOMETRIC_KEY] = isBiometricActive
        }
    }
    fun getBiometricSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[BIOMETRIC_KEY] ?: false
        }
    }

    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[THEME_KEY] ?: false
        }
    }
    suspend fun saveThemeSetting(isDarkModeActive : Boolean) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDarkModeActive
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: UserRepository? = null

        private val THEME_KEY = booleanPreferencesKey("theme_setting")
        private val BIOMETRIC_KEY = booleanPreferencesKey("biometric_setting")

        private val LOGIN_KEY = booleanPreferencesKey("login_string")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val UID_KEY = stringPreferencesKey("uid")
        private val FIRST_NAME_KEY = stringPreferencesKey("firstname")
        private val LAST_NAME_KEY = stringPreferencesKey("lastname")
        private val EMAIL_KEY = stringPreferencesKey("email")

        fun getInstance(
            dataStore: DataStore<Preferences>,
            apiService: ApiService
        ): UserRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = UserRepository(dataStore, apiService)
                INSTANCE = instance
                instance
            }
        }
    }
}