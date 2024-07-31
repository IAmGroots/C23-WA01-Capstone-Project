package com.example.capstoneproject.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.capstoneproject.MainActivity
import com.example.capstoneproject.data.response.LoginData
import com.example.capstoneproject.data.response.UserProfile
import com.example.capstoneproject.databinding.ActivityLoginBinding
import com.example.capstoneproject.preferences.ViewModelFactory
import com.example.capstoneproject.data.result.Result
import com.example.capstoneproject.ui.register.RegisterActivity
import java.util.concurrent.Executor
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModelFactory: ViewModelFactory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[LoginViewModel::class.java]

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        getBiometric()

        binding.btnLogin.setOnClickListener {
            viewModel.setLoading(true)
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            when {
                email.isEmpty() -> {
                    binding.etEmail.error = "Please enter your email"
                }

                password.isEmpty() -> {
                    binding.etPassword.error = "Please enter your password"
                }

                else -> {
                    if (isEmailValid(email)) {
                        if (password.length >= 8) {
                            signIn(email, password)
                        } else {
                            Toast.makeText(
                                this,
                                "Password must be at least 8 characters",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        binding.etEmail.error = "Invalid e-mail address format"
                    }
                }
            }
        }


        binding.regishere.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        viewModel.getBiometric().observe(this) { isEnableBiometric ->
            binding.btnBiometric.visibility = if (isEnableBiometric) View.VISIBLE else View.GONE
        }

        binding.btnBiometric.setOnClickListener {
            val biometricManager = BiometricManager.from(this)
            when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
                BiometricManager.BIOMETRIC_SUCCESS -> {
                    Log.d("Biometric", "This app can authenticate using biometrics")
                    createBiometricListener()
                    createPromptInfo()
                    biometricPrompt.authenticate(promptInfo)
                }

                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                    Log.d("Biometric", "There are no biometric features available on this device")
                }

                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                    Log.d("Biometric", "Currently the Biometric feature is not available")
                }

                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                    Log.d("Biometric", "The device does not have biometric features")
                }

                else -> {
                    Log.d("Biometric", "Something went wrong")
                }
            }
        }

        binding.textforgotpassword.setOnClickListener {
//            val email = binding.etEmail.text.toString()
//            val intent = Intent(this, ResetPasswordActivity::class.java)
//            intent.putExtra("email", email)
//            startActivity(intent)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.btnLogin.isEnabled = false
        } else {
            binding.progressBar.visibility = View.GONE
            binding.btnLogin.isEnabled = true
        }
    }

    private fun isEmailValid(email: String): Boolean {
        val emailRegex = ("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+")
        val pattern = Pattern.compile(emailRegex)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    fun signIn(email: String, password: String) {
        viewModel.login(email, password).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    viewModel.setLoading(true)
                }

                is Result.Success -> {
                    viewModel.setLoading(false)
                    Log.e("OUT", "OUT")
                    result.data.loginData?.let { setData(it) }
                }

                is Result.Error -> {
                    viewModel.setLoading(false)
                    Toast.makeText(this, "Silahkan periksa kembali email dan password Anda", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getBiometric() {
        viewModel.getBiometric().observe(this) { isEnableBiometric ->
            if (isEnableBiometric) {
                binding.btnBiometric.visibility = View.VISIBLE
            } else {
                binding.btnBiometric.visibility = View.GONE
            }
        }
    }

    private fun setData(loginData: LoginData) {
        val token = loginData.accessToken ?: ""
        val uid = loginData.profile?.uuid ?: ""
        val firstname = loginData.profile?.firstname ?: ""
        val lastname = loginData.profile?.lastname ?: ""
        val email = loginData.profile?.email ?: ""
        val mobile = loginData.profile?.mobile ?: ""
        val state = loginData.profile?.state.toString() ?: ""
        val city = loginData.profile?.city.toString() ?: ""
        val address = loginData.profile?.address1.toString() ?: ""

        val profile = UserProfile(
            token = token,
            uid = uid,
            firstName = firstname,
            lastName = lastname,
            email = email,
            mobile = mobile,
            state = state,
            city = city,
            address = address,
        )
        viewModel.saveProfile(profile)
        viewModel.saveLogin(true)
        viewModel.userProfile.observe(this) { profile ->
            Log.d("FIX BUG", "Login Activity : Function SetData")
            Log.d("FIX BUG", "uid: ${profile.uid}")
            Log.d("FIX BUG", "firstName: ${profile.firstName}")
            Log.d("FIX BUG", "lastName: ${profile.lastName}")
            Log.d("FIX BUG", "email: ${profile.email}")
            Log.d("FIX BUG", "token: ${profile.token}")
            Log.d("FIX BUG", "Login status saved")
        }

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun createBiometricListener() {
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt =
            BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(
                        this@LoginActivity,
                        "Authenticated Successful",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(
                        this@LoginActivity,
                        "Authenticated Unsuccessful",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(this@LoginActivity, errString.toString(), Toast.LENGTH_SHORT)
                        .show()
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
