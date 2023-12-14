package com.example.capstoneproject.ui.login

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Toast
import com.example.capstoneproject.databinding.ActivityResetPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResetPasswordBinding
    private lateinit var auth: FirebaseAuth
    var email: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        setFocusable()

        email = intent.getStringExtra("email").toString()
        binding.etEmail.setText(email)

        binding.btnSend.setOnClickListener {
            val etEmail = binding.etEmail.text.toString()
            if (binding.etEmail.text.toString().isEmpty()) {
                Toast.makeText(this, "Enter the email", Toast.LENGTH_SHORT).show()
            } else {
                auth.sendPasswordResetEmail(etEmail).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Check your email", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Send email failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.btnBack.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
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
    }
}