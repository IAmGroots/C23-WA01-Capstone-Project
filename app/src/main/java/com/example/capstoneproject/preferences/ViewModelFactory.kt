package com.example.capstoneproject.preferences

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.capstoneproject.MainViewModel
import com.example.capstoneproject.data.di.Injection
import com.example.capstoneproject.data.repository.UserRepository
import com.example.capstoneproject.ui.articles.ArticlesViewModel
import com.example.capstoneproject.ui.articles.detail_article.DetailViewModel
import com.example.capstoneproject.ui.home.HomeViewModel
import com.example.capstoneproject.ui.login.LoginViewModel
import com.example.capstoneproject.ui.otp.OtpViewModel
import com.example.capstoneproject.ui.profile.ProfileViewModel
import com.example.capstoneproject.ui.profile.edit_profile.EditProfileViewModel
import com.example.capstoneproject.ui.register.RegisterViewModel
import com.example.capstoneproject.ui.wifi.WifiViewModel

class ViewModelFactory(private val repository: UserRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository) as T
            }
            modelClass.isAssignableFrom(EditProfileViewModel::class.java) -> {
                EditProfileViewModel(repository) as T
            }
            modelClass.isAssignableFrom(WifiViewModel::class.java) -> {
                WifiViewModel(repository) as T
            }
            modelClass.isAssignableFrom(OtpViewModel::class.java) -> {
                OtpViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ArticlesViewModel::class.java) -> {
                ArticlesViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideUserRepository(context))
            }.also { instance = it }
    }
}