package com.example.capstoneproject.ui.usage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.capstoneproject.MainActivity
import com.example.capstoneproject.databinding.ActivityUsageBinding

class UsageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUsageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsageBinding.inflate(layoutInflater)
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