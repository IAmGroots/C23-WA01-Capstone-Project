package com.example.capstoneproject.ui.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.os.CountDownTimer
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.capstoneproject.MainActivity
import com.example.capstoneproject.R
import com.example.capstoneproject.databinding.ActivityLoginBinding
import com.example.capstoneproject.preferences.SettingPreferences
import com.example.capstoneproject.preferences.ViewModelFactory
import com.example.capstoneproject.preferences.dataStore
import com.example.capstoneproject.model.DataUser
import com.example.capstoneproject.ui.otp.OTPEmailActivity
import com.example.capstoneproject.ui.register.RegisterActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import java.util.concurrent.Executor

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private var db = Firebase.firestore
//    private lateinit var firebaseDatabase: FirebaseDatabase
//    private lateinit var databaseReference: DatabaseReference

    private var attemptsRemaining = 3 // Jumlah percobaan tersisa
    private var isCountdownActive = false // Apakah countdown aktif
    private lateinit var countdownTimer: CountDownTimer
    private var countdownTimeLeft: Long = 0
    private var isLoggingIn = false
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo


    override fun onCreate(savedInstanceState: Bundle?) {
        val preferences = SettingPreferences.getInstance(application.dataStore)
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(preferences)
        )[LoginViewModel::class.java]


        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        getBiometric()

//        firebaseDatabase = FirebaseDatabase.getInstance()
//        databaseReference = firebaseDatabase.reference.child("users")


        viewModel.getEmail().observe(this) { email ->
            if (email == "empty") {
                binding.etEmail.text = Editable.Factory.getInstance().newEditable("")
            } else {
                binding.etEmail.text = Editable.Factory.getInstance().newEditable(email)
            }
        }

        setFocusable()

        binding.btnLogin.setOnClickListener {
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
                    if (password.length >= 8) {
                        Log.e("email", email)
                        Log.e("password", password)
                        /*viewModel.login(email, password)*/

                        signIn(email, password)
                    } else {
                        Toast.makeText(this, "Please check your password!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }

        viewModel.loggedInUser.observe(this) { userLogin ->
            isLoggingIn = false
            if (userLogin != null) {
                Toast.makeText(this@LoginActivity, "SignIn is success", Toast.LENGTH_SHORT).show()

                /*saveUserDataToSharedPreferences(userLogin)*/

                setData()

                MainActivity.isLogin = true
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            } else {
                // Jika login gagal, kurangi percobaan dan mulai countdown jika sudah 3 kali salah
                attemptsRemaining--
                if (attemptsRemaining == 0 && !isCountdownActive) {
                    Toast.makeText(
                        this@LoginActivity,
                        "SignIn is failed. Remaining attempts: $attemptsRemaining",
                        Toast.LENGTH_SHORT
                    ).show()
                    startCountdown()
                } else if (isCountdownActive) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Coba lagi dalam ${countdownTimeLeft / 1000} detik",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "SignIn is failed. Remaining attempts: $attemptsRemaining",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding.regishere.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        viewModel.getBiometric().observe(this) { isEnableBiometric ->
            if (isEnableBiometric) {
                binding.btnBiometric.visibility = View.VISIBLE
            } else {
                binding.btnBiometric.visibility = View.GONE
            }
        }


        binding.btnBiometric.setOnClickListener {
            val biometricManager = BiometricManager.from(this)
            when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
                BiometricManager.BIOMETRIC_SUCCESS -> {
                    Toast.makeText(
                        this,
                        "App can authenticate is using bimetric",
                        Toast.LENGTH_SHORT
                    ).show()
                    createBiometricListener()
                    createPromptInfo()
                    biometricPrompt.authenticate(promptInfo)
                }

                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                    Toast.makeText(
                        this,
                        "No biometric feature available on this device",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                    Toast.makeText(
                        this,
                        "Biometric feature are currently unavailable",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                    Toast.makeText(this, "Device not enable biometric feature", Toast.LENGTH_SHORT)
                        .show()
                }

                else -> {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.textforgotpassword.setOnClickListener {
            val email = binding.etEmail.text.toString()
            var intent= Intent(this, resetPasswordActivity::class.java)
            intent.putExtra("email", email)
            startActivity(intent)
        }
    }

    private fun signIn(email :String, password: String ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener (this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "SignIn Success", Toast.LENGTH_SHORT).show()
                    MainActivity.isLogin = true
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "SignIn Failed", Toast.LENGTH_SHORT).show()
                }
            }
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

    private fun setData() {
        val userID = FirebaseAuth.getInstance().currentUser!!.uid
        val set = db.collection("user").document(userID)
        set.get().addOnSuccessListener {
            if (it != null) {
                val id = userID
                val firstname = it.data?.get("firstname")?.toString()
                val lastname = it.data?.get("lastname")?.toString()
                val email = it.data?.get("email")?.toString()
                val mobile = it.data?.get("mobile")?.toString()
                val plan = it.data?.get("plan")?.toString()

                Log.e("login", id)
                Log.e("login", firstname.toString())
                Log.e("login", lastname.toString())
                Log.e("login", email.toString())
                Log.e("login", mobile.toString())
                Log.e("login", plan.toString())
                val sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("id", id)
                editor.putString("firstName", firstname)
                editor.putString("lastName", lastname)
                editor.putString("email", email)
                editor.putString("phone", mobile)
                editor.putString("plan", plan)
                editor.apply()
            }
        }
            .addOnFailureListener {
                Toast.makeText(this, "Failed retrieve firestore data!", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    /*private fun saveUserDataToSharedPreferences(user: DataUser) {
        val sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("uid", user.id)
        editor.putString("firstName", user.firstname)
        editor.putString("lastName", user.lastname)
        editor.putString("email", user.email)
        editor.putString("phone", user.mobile)
        editor.putString("plan", user.plan)
        editor.apply()
    }*/

    private fun startCountdown() {
        isCountdownActive = true
        countdownTimer = object : CountDownTimer(300000, 1000) { // 300000 ms = 5 menit
            override fun onTick(millisUntilFinished: Long) {
                countdownTimeLeft = millisUntilFinished // Simpan waktu yang tersisa
            }

            override fun onFinish() {
                isCountdownActive = false
                Toast.makeText(
                    this@LoginActivity,
                    "Anda dapat mencoba login lagi",
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
                    Toast.makeText(this@LoginActivity, "Authenticated Success", Toast.LENGTH_SHORT)
                        .show()
                    MainActivity.isLogin = true
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(this@LoginActivity, "Authenticated Failed", Toast.LENGTH_SHORT)
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

    override fun onDestroy() {
        super.onDestroy()
        if (isCountdownActive) {
            countdownTimer.cancel() // Hentikan countdown jika aktiv
        }
    }
}
