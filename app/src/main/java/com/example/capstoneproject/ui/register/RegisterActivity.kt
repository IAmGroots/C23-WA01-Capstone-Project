package com.example.capstoneproject.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.capstoneproject.MainActivity
import com.example.capstoneproject.databinding.ActivityRegisterBinding
import com.example.capstoneproject.ui.login.LoginActivity
import com.example.capstoneproject.ui.otp.OTPActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, OTPActivity::class.java))
        }
    }
}