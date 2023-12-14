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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFocusable()

        binding.btnRegister.setOnClickListener {
            val firstname = binding.etFirstName.text.toString()
            val lastname = binding.etLastName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            when {
                firstname.isEmpty() -> {
                    binding.etFirstName.error = "Please enter your first name"
                }

                lastname.isEmpty() -> {
                    binding.etLastName.error = "Please enter your last name"
                }

                email.isEmpty() -> {
                    binding.etEmail.error = "Please enter your email"
                }

                password.isEmpty() -> {
                    binding.etPassword.error = "Please enter your password"
                }

                confirmPassword.isEmpty() -> {
                    binding.etConfirmPassword.error = "Please enter your password"
                }

                else -> {
                    if (firstname.length < 2 || firstname.length > 150) {
                        if (lastname.length < 2 || lastname.length > 150) {
                            if (isEmailValid(email)) {
                                if (password.length >= 8) {
                                    if (password == confirmPassword) {

                                        val firestore = FirebaseFirestore.getInstance()
                                        val usersRef = firestore.collection("user")
                                        val query = usersRef.whereEqualTo("email", email)

                                        query.get().addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                val documents = task.result
                                                if (documents != null && !documents.isEmpty) {
                                                    // Email sudah digunakan
                                                    Toast.makeText(
                                                        this@RegisterActivity,
                                                        "Email is already in use!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                } else {
                                                    // Email belum digunakan, lanjutkan ke verifikasi OTP
                                                    val intent = Intent(
                                                        this@RegisterActivity,
                                                        OTPEmailActivity::class.java
                                                    )
                                                    intent.putExtra("firstname", firstname)
                                                    intent.putExtra("lastname", lastname)
                                                    intent.putExtra("email", email)
                                                    intent.putExtra("password", password)
                                                    startActivity(intent)
                                                }
                                            } else {
                                                // Gagal melakukan pengecekan
                                                Toast.makeText(
                                                    this@RegisterActivity,
                                                    "Failed to check email existence!",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    } else {
                                        Toast.makeText(
                                            this,
                                            "Confirmed passwords do not match",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    Toast.makeText(
                                        this,
                                        "Password must be at least 8 characters",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                binding.etEmail.error =
                                    "Invalid e-mail address format"
                            }
                        } else {
                            binding.etLastName.error =
                                "Name must be a minimum of 2 characters and a maximum of 150 characters"
                        }
                    } else {
                        binding.etFirstName.error =
                            "Name must be a minimum of 2 characters and a maximum of 150 characters"
                    }
                }
            }
        }

        binding.btnBack.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        binding.loginhere.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
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

