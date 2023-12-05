package com.example.capstoneproject.ui.register

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.viewModels
import com.example.capstoneproject.databinding.ActivityRegisterBinding
import com.example.capstoneproject.ui.login.LoginActivity
import com.example.capstoneproject.ui.otp.OTPEmailActivity
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.database.ValueEventListener
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = Firebase.firestore

        setFocusable()

        binding.btnRegister.setOnClickListener {
            val firstname = binding.etFirstName.text.toString()
            val lastname = binding.etLastName.text.toString()
            val email = binding.etEmail.text.toString()
            val mobile = binding.etMobile.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()
            if (firstname.isNotEmpty() && lastname.isNotEmpty() && email.isNotEmpty() && mobile.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password == confirmPassword) {
                    var intent= Intent(this@RegisterActivity, OTPEmailActivity::class.java)
                    intent.putExtra("firstname", firstname)
                    intent.putExtra("lastname", lastname)
                    intent.putExtra("email", email)
                    intent.putExtra("mobile", mobile)
                    intent.putExtra("password", password)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "The password confirm is not match! ", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Fill all the data!", Toast.LENGTH_SHORT).show()
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
        binding.etFirstName.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    binding.etFirstName.isFocusable = true
                    binding.etFirstName.isFocusableInTouchMode = true
                }

                MotionEvent.ACTION_UP -> {
                    binding.etFirstName.requestFocus()
                }
            }
            false
        }

        binding.etLastName.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    binding.etLastName.isFocusable = true
                    binding.etLastName.isFocusableInTouchMode = true
                }

                MotionEvent.ACTION_UP -> {
                    binding.etLastName.requestFocus()
                }
            }
            false
        }

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

        binding.etMobile.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    binding.etMobile.isFocusable = true
                    binding.etMobile.isFocusableInTouchMode = true
                }

                MotionEvent.ACTION_UP -> {
                    binding.etMobile.requestFocus()
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

        binding.etConfirmPassword.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    binding.etConfirmPassword.isFocusable = true
                    binding.etConfirmPassword.isFocusableInTouchMode = true
                }

                MotionEvent.ACTION_UP -> {
                    binding.etConfirmPassword.requestFocus()
                }
            }
            false
        }
    }
}

