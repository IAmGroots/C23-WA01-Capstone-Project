package com.example.capstoneproject.ui.profile.theme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.capstoneproject.R
import com.example.capstoneproject.databinding.ActivityDarkModeBinding
import com.example.capstoneproject.databinding.ActivityHistoryBinding
import com.example.capstoneproject.preferences.SettingPreferences
import com.example.capstoneproject.preferences.ViewModelFactory
import com.example.capstoneproject.preferences.dataStore
import com.example.capstoneproject.ui.login.LoginViewModel

class DarkModeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDarkModeBinding
    private lateinit var viewModel: DarkModeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDarkModeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val preferences = SettingPreferences.getInstance(application.dataStore)
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(preferences)
        )[DarkModeViewModel::class.java]

        setupToolbar()

        viewModel.getTheme().observe(this) { isDarkModeActive ->
            AppCompatDelegate.setDefaultNightMode(if(isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
            binding.rbOnDarkMode.isChecked = isDarkModeActive
            binding.rbOffDarkMode.isChecked = !isDarkModeActive
        }

        binding.rbOnDarkMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.saveTheme(true)
                binding.rbOnDarkMode.isChecked = true
                binding.rbOffDarkMode.isChecked = false
            }
        }

        binding.rbOffDarkMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                setAppTheme(false)
                viewModel.saveTheme(false)
                binding.rbOnDarkMode.isChecked = false
                binding.rbOffDarkMode.isChecked = true
            }
        }

//        setupRadioButtons()
//        setupViewModel()
    }

    private fun setupToolbar() {
        val toolbar = binding.toolbar

        toolbar.setNavigationOnClickListener {
            super.onBackPressed()
        }
    }

    private fun setupViewModel() {
        viewModel.getTheme().observe(this) { isDarkModeActive ->
            if (isDarkModeActive) {
                binding.rbOnDarkMode.isChecked = true
                binding.rbOffDarkMode.isChecked = false
            } else {
                binding.rbOnDarkMode.isChecked = false
                binding.rbOffDarkMode.isChecked = true
            }
        }
    }

    private fun setupRadioButtons() {
        binding.rbOnDarkMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                setAppTheme(true)
                binding.rbOnDarkMode.isChecked = true
                binding.rbOffDarkMode.isChecked = false
            }
        }

        binding.rbOffDarkMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                setAppTheme(false)
                binding.rbOnDarkMode.isChecked = false
                binding.rbOffDarkMode.isChecked = true
            }
        }
    }

    private fun setAppTheme(isDarkMode: Boolean) {
//        if (isDarkMode) {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//        } else {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//        }
        viewModel.saveTheme(isDarkMode)
    }
}
