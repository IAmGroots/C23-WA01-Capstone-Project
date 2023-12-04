package com.example.capstoneproject.ui.register

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.capstoneproject.databinding.ActivityRegisterBinding
import com.example.capstoneproject.ui.login.LoginActivity
import com.example.capstoneproject.ui.login.LoginViewModel
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.database.ValueEventListener
import com.example.capstoneproject.ui.otp.OTPActivity
import com.example.capstoneproject.ui.otp.OTPViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    /*private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference*/
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")*/
        setFocusable()

        binding.loginhere.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnRegister.setOnClickListener {
            val firstname = binding.etFirstName.text.toString()
            val lastname = binding.etLastName.text.toString()
            val mobile = binding.etMobile.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            Log.e("firstname", firstname)
            Log.e("lastname", lastname)
            Log.e("mobile", mobile)
            Log.e("email", email)
            Log.e("password", password)
            Log.e("confirmPassword", confirmPassword)

            if (firstname.isNotEmpty() && lastname.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && mobile.isNotEmpty()) {
                if (password == confirmPassword) {
                    viewModel.userEmail.observe(this, Observer { check ->
                        val firstname = binding.etFirstName.text.toString()
                        val lastname = binding.etLastName.text.toString()
                        val mobile = binding.etMobile.text.toString()
                        val email = binding.etEmail.text.toString()
                        val password = binding.etPassword.text.toString()
                        if (check != null) {
                            Toast.makeText(
                                this@RegisterActivity,
                                "This email already used! Try to SignIn",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            viewModel.userMobile.observe(this, Observer { checkMobile ->
                                if (checkMobile != null) {
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        "This mobile number already used!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    val intent =
                                        Intent(this@RegisterActivity, OTPActivity::class.java)
                                    intent.putExtra("firstname", firstname)
                                    intent.putExtra("lastname", lastname)
                                    intent.putExtra("mobile", mobile)
                                    intent.putExtra("email", email)
                                    intent.putExtra("password", password)
                                    startActivity(intent)
                                }
                            })
                        }
                    })
                    viewModel.checkEmail(email)
                    viewModel.checkMobile(mobile)
                } else {
                    Toast.makeText(this, "The password confirm is not match! ", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(this, "Fill all the data!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.loginhere.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
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

