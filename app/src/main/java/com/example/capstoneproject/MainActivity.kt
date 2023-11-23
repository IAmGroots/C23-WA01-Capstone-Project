package com.example.capstoneproject

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavAction
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.capstoneproject.databinding.ActivityMainBinding
import com.example.capstoneproject.ui.home.HomeFragment
import com.example.capstoneproject.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // untuk sementara false
//        val isLogin = false
        if (!isLogin) {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        val navView: BottomNavigationView = binding.navView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // masih ada Bug
        // untuk navController dibagian Bottom Navigation
//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            when (destination.id) {
//                R.id.navigation_home -> {
//                    Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
//                }
//                R.id.navigation_news -> {
//                    Toast.makeText(this, "News", Toast.LENGTH_SHORT).show()
//                }
//                R.id.navigation_settings -> {
//                    Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }

        navView.setupWithNavController(navController)
    }

    companion object {
        var isLogin = false
    }
}