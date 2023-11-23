package com.example.capstoneproject.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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

        binding.loginhere.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnregis.setOnClickListener {
            if(binding.firstname.text.toString().isEmpty() || binding.lastname.text.toString().isEmpty() || binding.emailtext.text.toString().isEmpty() || binding.passwordtext.text.toString().isEmpty() || binding.passwordconfirm.text.toString().isEmpty()) {
                Toast.makeText(this, "Fill all the data!", Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(this, OTPActivity::class.java))
            }
        }
    }
}
