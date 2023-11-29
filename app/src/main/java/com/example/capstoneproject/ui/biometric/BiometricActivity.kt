package com.example.capstoneproject.ui.biometric

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.capstoneproject.MainActivity
import com.example.capstoneproject.R
import com.example.capstoneproject.databinding.ActivityBiometricBinding
import com.example.capstoneproject.databinding.ActivityLoginBinding
import com.example.capstoneproject.preferences.SettingPreferences
import com.example.capstoneproject.preferences.ViewModelFactory
import com.example.capstoneproject.preferences.dataStore
import com.example.capstoneproject.ui.login.LoginActivity
import com.example.capstoneproject.ui.login.LoginViewModel
import java.util.concurrent.Executor

class BiometricActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBiometricBinding
    private lateinit var viewModel: BiometricViewModel

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBiometricBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getBiometric()

        // login with email
        binding.btnLoginEmail.setOnClickListener { startActivity(Intent(this, LoginActivity::class.java)) }
        // login with biometric
        binding.btnLoginBiometric.setOnClickListener { setActionBiometric() }
    }

    private fun getBiometric() {
        val preferences = SettingPreferences.getInstance(application.dataStore)
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(preferences)
        )[BiometricViewModel::class.java]

        var hasBiometric = false
        viewModel.getHasBiometric().observe(this) { hasBiometric = it }
        viewModel.getBiometric().observe(this) { isEnableBiometric ->
            if (isEnableBiometric && hasBiometric) {
                binding.btnLoginEmail.visibility = View.GONE
                binding.btnLoginBiometric.visibility = View.VISIBLE
            } else {
                binding.btnLoginEmail.visibility = View.VISIBLE
                binding.btnLoginBiometric.visibility = View.GONE
            }
        }
    }

    private fun setActionBiometric() {
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                createBiometricListener()
                createPromptInfo()
                biometricPrompt.authenticate(promptInfo)
            }
            else -> {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createBiometricListener() {
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Toast.makeText(this@BiometricActivity, "Authenticated Success", Toast.LENGTH_SHORT).show()
                MainActivity.isLogin = true
                val intent = Intent(this@BiometricActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(this@BiometricActivity, "Authenticated Failed", Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(this@BiometricActivity, errString.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun createPromptInfo() {
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for App")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("CANCEL BIOMETRIC")
            .build()
    }
}