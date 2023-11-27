package com.example.capstoneproject.ui.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.capstoneproject.MainActivity
import com.example.capstoneproject.databinding.ActivityLoginBinding
import com.example.capstoneproject.model.dataUser
import com.example.capstoneproject.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    private var attemptsRemaining = 3 // Jumlah percobaan tersisa
    private var isCountdownActive = false // Apakah countdown aktif
    private lateinit var countdownTimer: CountDownTimer
    private var countdownTimeLeft: Long = 0
    private var isLoggingIn = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnlogin.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                isLoggingIn = true
                Log.e("email", email)
                Log.e("password", password)
                viewModel.login(email, password)
            } else {
                Toast.makeText(this, "Fill all the data!", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.loggedInUser.observe(this, Observer { userLogin ->
            isLoggingIn = false
            if (userLogin != null) {
                Toast.makeText(this@LoginActivity, "SignIn is success", Toast.LENGTH_SHORT).show()
                saveUserDataToSharedPreferences(userLogin)
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
        })

        binding.regishere.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun saveUserDataToSharedPreferences(user: dataUser) {
        val sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("firstName", user.firstname)
        editor.putString("lastName", user.lastname)
        editor.putString("email", user.email)
        editor.apply()
    }

    private fun startCountdown() {
        isCountdownActive = true
        countdownTimer = object : CountDownTimer(300000, 1000) { // 300000 ms = 5 menit
            override fun onTick(millisUntilFinished: Long) {
                countdownTimeLeft = millisUntilFinished // Simpan waktu yang tersisa
            }

            override fun onFinish() {
                isCountdownActive = false
                Toast.makeText(this@LoginActivity, "Anda dapat mencoba login lagi", Toast.LENGTH_SHORT).show()
                attemptsRemaining = 3
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isCountdownActive) {
            countdownTimer.cancel() // Hentikan countdown jika aktiv
        }
    }
}
