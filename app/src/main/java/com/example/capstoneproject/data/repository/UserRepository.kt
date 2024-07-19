package com.example.capstoneproject.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.capstoneproject.data.response.OneArticlesResponse
import com.example.capstoneproject.data.response.ArticlesResponse
import com.example.capstoneproject.data.response.ListWifiResponse
import com.example.capstoneproject.data.response.LoginResponse
import com.example.capstoneproject.data.response.OtpResponse
import com.example.capstoneproject.data.response.RegisterResponse
import com.example.capstoneproject.data.response.UpdateUserResponse
import com.example.capstoneproject.data.response.UserProfile
import com.example.capstoneproject.data.retrofit.ApiService
import com.example.capstoneproject.data.result.Result
import com.example.capstoneproject.preferences.ProfilePreferences.Companion.ADDRESS_KEY
import com.example.capstoneproject.preferences.ProfilePreferences.Companion.CITY_KEY
import com.example.capstoneproject.preferences.ProfilePreferences.Companion.EMAIL_KEY
import com.example.capstoneproject.preferences.ProfilePreferences.Companion.FIRST_NAME_KEY
import com.example.capstoneproject.preferences.ProfilePreferences.Companion.LAST_NAME_KEY
import com.example.capstoneproject.preferences.ProfilePreferences.Companion.MOBILE_KEY
import com.example.capstoneproject.preferences.ProfilePreferences.Companion.STATE_KEY
import com.example.capstoneproject.preferences.ProfilePreferences.Companion.TOKEN_KEY
import com.example.capstoneproject.preferences.ProfilePreferences.Companion.UID_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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
        address1: String,
        city: String,
        state: String
    ): LiveData<Result<UpdateUserResponse>> = liveData {
        emit(Result.Loading)
        try {
            Log.e(
                "Repository",
                "Token: $token, Data: $firstname, $lastname, $mobile, $address1, $city, $state"
            )
            val result =
                apiService.updateUser(token, firstname, lastname, mobile, address1, city, state)
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

    fun getListArticle(token: String): LiveData<Result<ArticlesResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService.getListArticle(token)
            emit(Result.Success(result))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getArticle(id: String, token: String): LiveData<Result<OneArticlesResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService.getArticle(id, token)
            emit(Result.Success(result))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

    fun validateOtp(uuid: String, otp: String): LiveData<Result<OtpResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService.validateOtp(uuid, otp)
            emit(Result.Success(result))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

    // Menyimpan data profil
    suspend fun saveProfile(profile: UserProfile) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = profile.token
            preferences[UID_KEY] = profile.token
            preferences[FIRST_NAME_KEY] = profile.firstName
            preferences[LAST_NAME_KEY] = profile.lastName
            preferences[EMAIL_KEY] = profile.email
            preferences[MOBILE_KEY] = profile.mobile
            preferences[STATE_KEY] = profile.state
            preferences[CITY_KEY] = profile.city
            preferences[ADDRESS_KEY] = profile.address
        }
    }

    // Mengambil data profil
    fun getProfile(): Flow<UserProfile> {
        return dataStore.data.map { preferences ->
            UserProfile(
                token = preferences[TOKEN_KEY] ?: "",
                uid = preferences[UID_KEY] ?: "",
                firstName = preferences[FIRST_NAME_KEY] ?: "",
                lastName = preferences[LAST_NAME_KEY] ?: "",
                email = preferences[EMAIL_KEY] ?: "",
                mobile = preferences[MOBILE_KEY] ?: "",
                state = preferences[STATE_KEY] ?: "",
                city = preferences[CITY_KEY] ?: "",
                address = preferences[ADDRESS_KEY] ?: ""
            )
        }
    }

    suspend fun clearProfile() {
        dataStore.edit { preferences ->
            preferences[LOGIN_KEY] = false
            preferences[TOKEN_KEY] = ""
            preferences[UID_KEY] = ""
            preferences[FIRST_NAME_KEY] = ""
            preferences[LAST_NAME_KEY] = ""
            preferences[EMAIL_KEY] = ""
            preferences[MOBILE_KEY] = ""
            preferences[STATE_KEY] = ""
            preferences[CITY_KEY] = ""
            preferences[ADDRESS_KEY] = ""
            preferences[LOGIN_KEY] = false
            preferences[BIOMETRIC_KEY] = false
            preferences[THEME_KEY] = false
        }
    }

    suspend fun saveBiometricSetting(isBiometricActive: Boolean) {
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

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDarkModeActive
        }
    }

    suspend fun saveLogin(isLogin: Boolean) {
        dataStore.edit { preferences ->
            preferences[LOGIN_KEY] = isLogin
        }
    }

    fun getLogin(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[LOGIN_KEY] ?: false
        }
    }

    suspend fun saveUID(uid : String) {
        dataStore.edit { preferences ->
            preferences[UID_KEY] = uid
        }
    }

    fun getUID(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[UID_KEY] ?: ""
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserRepository? = null

        private val LOGIN_KEY = booleanPreferencesKey("login_string")
        private val THEME_KEY = booleanPreferencesKey("theme_setting")
        private val BIOMETRIC_KEY = booleanPreferencesKey("biometric_setting")
//        private val TOKEN_KEY = stringPreferencesKey("token")
//        private val UID_KEY = stringPreferencesKey("uid")
//        private val FIRST_NAME_KEY = stringPreferencesKey("firstname")
//        private val LAST_NAME_KEY = stringPreferencesKey("lastname")
//        private val EMAIL_KEY = stringPreferencesKey("email")
//        private val MOBILE_KEY = stringPreferencesKey("mobile")
//        private val STATE_KEY = stringPreferencesKey("state")
//        private val CITY_KEY = stringPreferencesKey("city")
//        private val ADDRESS_KEY = stringPreferencesKey("address")

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