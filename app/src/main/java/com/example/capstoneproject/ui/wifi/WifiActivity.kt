package com.example.capstoneproject.ui.wifi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.capstoneproject.MainActivity
import com.example.capstoneproject.databinding.ActivityWifiBinding
import com.example.capstoneproject.ui.login.LoginActivity

class WifiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWifiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWifiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
    }

    private fun setupToolbar() {
        val toolbar = binding.toolbar

        toolbar.setNavigationOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}