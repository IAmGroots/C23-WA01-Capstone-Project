package com.example.capstoneproject.ui.profile.biometric

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import com.example.capstoneproject.R
import com.example.capstoneproject.databinding.ActivityBiometricBinding
import com.example.capstoneproject.databinding.ActivityDarkModeBinding
import com.example.capstoneproject.preferences.SettingPreferences
import com.example.capstoneproject.preferences.ViewModelFactory
import com.example.capstoneproject.preferences.dataStore
import com.example.capstoneproject.ui.profile.theme.DarkModeViewModel

class BiometricActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBiometricBinding
    private lateinit var viewModel: BiometricViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBiometricBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val preferences = SettingPreferences.getInstance(application.dataStore)
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(preferences)
        )[BiometricViewModel::class.java]

        setupToolbar()
        setupRadioButtons()
        setupViewModel()
    }

    private fun setupToolbar() {
        val toolbar = binding.toolbar

        toolbar.setNavigationOnClickListener {
            super.onBackPressed()
        }
    }

    private fun setupViewModel() {
        viewModel.getBiometric().observe(this) { isBiometricActive ->
            if (isBiometricActive) {
                binding.rbOnBiometric.isChecked = true
                binding.rbOffBiometric.isChecked = false
            } else {
                binding.rbOnBiometric.isChecked = false
                binding.rbOffBiometric.isChecked = true
            }
        }
    }

    private fun setupRadioButtons() {
        binding.rbOnBiometric.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                setBiometric(true)
                binding.rbOnBiometric.isChecked = true
                binding.rbOffBiometric.isChecked = false
            }
        }

        binding.rbOffBiometric.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                setBiometric(false)
                binding.rbOnBiometric.isChecked = false
                binding.rbOffBiometric.isChecked = true
            }
        }
    }

    private fun setBiometric(isBiometric: Boolean) {
        viewModel.saveBiometric(isBiometric)
    }
}