package com.example.capstoneproject.ui.profile.edit_profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Toast
import com.example.capstoneproject.MainActivity
import com.example.capstoneproject.databinding.ActivityEditProfileBinding
import com.example.capstoneproject.model.DataSourceUser

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFocusable()

        binding.btnCancel.setOnClickListener {
            // ??? Intent balik ProfileFragment

            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.btnSaveChanges.setOnClickListener {
            saveChanges()
            startActivity(Intent(this, MainActivity::class.java))

        }

        setupToolbar()
        loadUserDataFromSharedPreferences()
    }

    private fun setupToolbar() {
        val toolbar = binding.toolbar

        toolbar.setNavigationOnClickListener {
            super.onBackPressed()
        }
    }

    private fun loadUserDataFromSharedPreferences() {
        val sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val firstName = sharedPreferences.getString("firstName", "")
        val lastName = sharedPreferences.getString("lastName", "")
        val email = sharedPreferences.getString("email", "")
        val phone = sharedPreferences.getString("phone", "")

        // Menampilkan informasi pengguna di EditText pada profil
        binding.etFirstName.setText(firstName)
        binding.etLastName.setText(lastName)
        binding.etEmail.setText(email)
        binding.etMobile.setText(phone)
    }

    private fun saveChanges() {
        val sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val idString = sharedPreferences.getString("id", "")
        val loggedInUserId = idString?.toIntOrNull() ?: 0

        val firstName = binding.etFirstName.text.toString()
        val lastName = binding.etLastName.text.toString()
        val email = binding.etEmail.text.toString()
        val phone = binding.etMobile.text.toString()

        editor.putString("firstName", firstName)
        editor.putString("lastName", lastName)
        editor.putString("email", email)
        editor.putString("phone", phone)

        editor.apply()

        updateDataSourceUser(loggedInUserId, firstName, lastName, email, phone)
        Toast.makeText(this, "Data berhasil diubah", Toast.LENGTH_SHORT).show()
    }

    private fun updateDataSourceUser(
        userId: Int,
        firstName: String,
        lastName: String,
        email: String,
        phone: String
    ): Boolean {
        for (user in DataSourceUser.user) {
            if (user.id == userId) {
                user.firstname = firstName
                user.lastname = lastName
                user.email = email
                user.mobile = phone
                return true // Return true when the user data is successfully updated
            }
        }
        return false // Return false if user data update failed
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
    }
}

