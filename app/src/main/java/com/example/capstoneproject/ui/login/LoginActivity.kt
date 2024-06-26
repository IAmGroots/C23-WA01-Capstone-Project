package com.example.capstoneproject.ui.login

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.capstoneproject.MainActivity
import com.example.capstoneproject.R
import com.example.capstoneproject.data.response.LoginData
import com.example.capstoneproject.databinding.ActivityLoginBinding
import com.example.capstoneproject.preferences.ViewModelFactory
import com.example.capstoneproject.data.result.Result
import com.example.capstoneproject.ui.register.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import java.util.concurrent.Executor
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    lateinit var auth: FirebaseAuth
//    private lateinit var firestore: FirebaseFirestore
//    private var db = Firebase.firestore
    private var attemptsRemaining = 3 // Jumlah percobaan tersisa
    private var isCountdownActive = false // Apakah countdown aktif
    private lateinit var countdownTimer: CountDownTimer
    private var countdownTimeLeft: Long = 0
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

//        auth = FirebaseAuth.getInstance()
//        firestore = FirebaseFirestore.getInstance()

        getBiometric()
        setFocusable()


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
                            viewModel.isLoading.observe(this) {
                                showLoading(it)
                            }
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


        binding.btnBiometric.setOnClickListener{
            val biometricManager = BiometricManager.from(this)
            when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
                BiometricManager.BIOMETRIC_SUCCESS -> {
                    Log.d("Biometric", "This app can authenticate using biometrics")
                    createBiometricListener()
                    createPromptInfo()
                    biometricPrompt.authenticate(promptInfo)
                }
                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> { Log.d("Biometric", "There are no biometric features available on this device") }
                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> { Log.d("Biometric", "Currently the Biometric feature is not available") }
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> { Log.d("Biometric", "The device does not have biometric features") }
                else -> { Log.d("Biometric", "Something went wrong") }
            }
        }

        binding.textforgotpassword.setOnClickListener{
            val email = binding.etEmail.text.toString()
            val intent = Intent(this, ResetPasswordActivity::class.java)
            intent.putExtra("email", email)
            startActivity(intent)
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
            result?.let {
                when (it) {
                    is Result.Loading -> {
                        showLoading(true)
                    }

                    is Result.Success -> {
                        showLoading(false)
                        Log.e("OUT", "OUT")
                        it.data.let { response ->
                            response.loginData?.let { loginData ->
                                setData(loginData)
                                Log.e("IN", "IN")
                            }
                        }
                    }

                    is Result.Error -> {
                        showLoading(false)
                    }
                }
            }
        }
//        auth.signInWithEmailAndPassword(email, password)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
//                    setData()
//                    viewModel.setLoading(false)
//                } else {
//                    // Jika login gagal, kurangi percobaan dan mulai countdown jika sudah 3 kali salah
//                    attemptsRemaining--
//                    Log.e("Sec", attemptsRemaining.toString())
//                    if (attemptsRemaining == 0 && isCountdownActive) {
//                        Toast.makeText(
//                            this@LoginActivity,
//                            "The data you entered is incorrect, please check again.",
//                            Toast.LENGTH_SHORT
//                        ).show()
//
//                        val delayInMillis: Long = 2000
//                        Handler(Looper.getMainLooper()).postDelayed({
//                            Toast.makeText(
//                                this@LoginActivity,
//                                "Try again in ${countdownTimeLeft / 1000} seconds",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }, delayInMillis)
//                        viewModel.setLoading(false)
//                    } else {
//                        Toast.makeText(
//                            this@LoginActivity,
//                            "The data you entered is incorrect, please check again.",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        viewModel.setLoading(false)
//
//                        if (attemptsRemaining <= 0) {
//                            val delayInMillis: Long = 2000
//                            Handler(Looper.getMainLooper()).postDelayed({
//                                Toast.makeText(
//                                    this@LoginActivity,
//                                    "Remaining attempts: 0",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }, delayInMillis)
//                            startCountdown()
//                        } else {
//                            val delayInMillis: Long = 2000
//                            Handler(Looper.getMainLooper()).postDelayed({
//                                Toast.makeText(
//                                    this@LoginActivity,
//                                    "Remaining attempts: $attemptsRemaining",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }, delayInMillis)
//                        }
//                    }
//                }
//            }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setFocusable() {
        binding.etEmail.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    binding.etEmail.isFocusable = true
                    binding.etEmail.isFocusableInTouchMode = true
                }

                MotionEvent.ACTION_UP -> {
                    binding.etEmail.requestFocus()
                }
            }
            false
        }

        binding.etPassword.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    binding.etPassword.isFocusable = true
                    binding.etPassword.isFocusableInTouchMode = true
                }

                MotionEvent.ACTION_UP -> {
                    binding.etPassword.requestFocus()
                }
            }
            false
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

        viewModel.saveToken(token)
        viewModel.saveLogin(true)
        viewModel.saveUID(uid)
        viewModel.saveFirstname(firstname)
        viewModel.saveLastname(lastname)
        viewModel.saveEmail(email)


        Log.d("FIX BUG", "Login Activity : Function SetData")
        Log.d("FIX BUG", "UID: $uid")
        Log.d("FIX BUG", "UID: $firstname")
        Log.d("FIX BUG", "UID: $lastname")
        Log.d("FIX BUG", "UID: $email")
        Log.d("FIX BUG", "UID: $token")

        Log.d("FIX BUG", "Login status saved")

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

//    private fun setData() {
//        val userID = FirebaseAuth.getInstance().currentUser?.uid
//        db.collection("user").document(userID!!)
//        Log.e("FIX BUG", userID)
//        db.collection("user")
//            .whereEqualTo("uid", userID)
//            .limit(1)
//            .get()
//            .addOnSuccessListener { querySnapshot ->
//                if (!querySnapshot.isEmpty) {
//                    Log.e("Success", "Save Data")
//                    viewModel.saveLogin(true)
//                    viewModel.saveUID(userID)
//
//                    Log.d("FIX BUG", "Login Activity : Function SetData")
//                    val intent = Intent(this, MainActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//                    startActivity(intent)
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.d("Failed Retrieve Firestore", "Failed retrieve firestore data: ${exception.message}")
//            }
//    }

    private fun startCountdown() {
        binding.bgBtnLogin.apply {
            binding.btnLogin.isEnabled = false
            setBackgroundResource(R.drawable.cardview_border_disabled)
        }
        binding.bgBtnBiometric.apply {
            binding.bgBtnBiometric.isEnabled = false
            setBackgroundResource(R.drawable.cardview_border_disabled)
        }

        Log.d("FIX BUG", "Btn Disabled")
        isCountdownActive = true
        countdownTimer = object : CountDownTimer(300000, 1000) { // 300000 ms = 5 menit
            override fun onTick(millisUntilFinished: Long) {
                countdownTimeLeft = millisUntilFinished // Simpan waktu yang tersisa
                val secondsRemaining = millisUntilFinished / 1000
                if (secondsRemaining % 5 == 0L) {
                    val toastMessage = "Try again in: ${secondsRemaining}s"
                    Toast.makeText(this@LoginActivity, toastMessage, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFinish() {
                isCountdownActive = false
                binding.btnLogin.setBackgroundResource(R.drawable.cardview_border_no_padding)

                binding.bgBtnLogin.apply {
                    binding.btnLogin.isEnabled = true
                    setBackgroundResource(R.drawable.cardview_border_no_padding)
                }
                binding.bgBtnBiometric.apply {
                    binding.btnBiometric.isEnabled = true
                    setBackgroundResource(R.drawable.cardview_border_no_padding)
                }
                Log.d("FIX BUG", "Btn Enabled")
                Toast.makeText(
                    this@LoginActivity,
                    "You can try logging in again",
                    Toast.LENGTH_SHORT
                ).show()
                attemptsRemaining = 3
            }
        }.start()
    }

    private fun createBiometricListener() {
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt =
            BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(this@LoginActivity, "Authenticated Successful", Toast.LENGTH_SHORT)
                        .show()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(this@LoginActivity, "Authenticated Unsuccessful", Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(this@LoginActivity, errString.toString(), Toast.LENGTH_SHORT).show()
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

    override fun onDestroy() {
        super.onDestroy()
        if (isCountdownActive) {
            countdownTimer.cancel() // Hentikan countdown jika aktif
        }
    }
}
