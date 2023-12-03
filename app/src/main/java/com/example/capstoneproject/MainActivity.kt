package com.example.capstoneproject

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavAction
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.capstoneproject.databinding.ActivityMainBinding
import com.example.capstoneproject.preferences.SettingPreferences
import com.example.capstoneproject.preferences.ViewModelFactory
import com.example.capstoneproject.preferences.dataStore
import com.example.capstoneproject.ui.home.HomeFragment
import com.example.capstoneproject.ui.login.LoginActivity
import com.example.capstoneproject.ui.login.LoginViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        val preferences = SettingPreferences.getInstance(application.dataStore)
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(preferences)
        )[MainViewModel::class.java]

        viewModel.getTheme().observe(this) { isDarkModeEnabled ->
            if (isDarkModeEnabled) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // untuk sementara false
//        val isLogin = false
        if (!isLogin) {
            Log.d("DISINI", "DISINI")
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }


        
        val navView: BottomNavigationView = binding.navView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        navView.setupWithNavController(navController)
    }

    companion object {
        var isLogin = false
    }
}