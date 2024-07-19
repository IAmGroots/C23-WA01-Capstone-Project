package com.example.capstoneproject.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.capstoneproject.MainActivity
import com.example.capstoneproject.MainViewModel
import com.example.capstoneproject.databinding.ActivitySplashBinding
import com.example.capstoneproject.preferences.ViewModelFactory
import com.example.capstoneproject.ui.login.LoginActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val DURATION: Long = 1500
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModelFactory: ViewModelFactory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[MainViewModel::class.java]

        viewModel.getTheme().observe(this) { isDarkModeEnabled ->
            if (isDarkModeEnabled) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        var enableBiometric = false
        viewModel.getBiometric().observe(this) {
            enableBiometric = it
        }

        viewModel.getLogin().observe(this) { isLogin ->
            if (isLogin && enableBiometric) {
                Handler(Looper.getMainLooper()).postDelayed({
                    Log.d("FIX BUG", "Di Splash Activity : GetLogin Viewmodel | Dari Splash ke Login")
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }, DURATION)
            }
            else if (isLogin) {
                Handler(Looper.getMainLooper()).postDelayed({
                    Log.d("FIX BUG", "Di Splash Activity : GetLogin ViewModel | Dari Splash ke Home")
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }, DURATION)
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    Log.d("FIX BUG", "Di Splash Activity : GetLogin ViewModel | Dari Splash ke Login")
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }, DURATION)
            }
        }
    }
}