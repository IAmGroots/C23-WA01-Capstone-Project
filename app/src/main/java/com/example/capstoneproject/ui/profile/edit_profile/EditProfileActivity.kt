package com.example.capstoneproject.ui.profile.edit_profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.capstoneproject.MainActivity
import com.example.capstoneproject.R
import com.example.capstoneproject.data.result.Result
import com.example.capstoneproject.databinding.ActivityEditProfileBinding
import com.example.capstoneproject.preferences.ViewModelFactory
import com.google.firebase.firestore.FirebaseFirestore

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var viewModel: EditProfileViewModel
    private lateinit var user_id: String
    private lateinit var oldPhone: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModelFactory: ViewModelFactory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[EditProfileViewModel::class.java]

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.isCityActive.observe(this) {
            showCity(it)
        }

//        setFocusable()
        getDataFromDB()
        getLocation()

        binding.btnCancel.setOnClickListener {
            super.onBackPressed()
        }
        binding.btnSaveChanges.setOnClickListener {
            updateDataToDB()
        }
//        binding.btnSaveChanges.setOnClickListener {
//            val firstName = binding.etFirstName.text.toString()
//            val lastName = binding.etLastName.text.toString()
//            val phone = binding.etMobile.text.toString()
//
//            when {
//                firstName.isEmpty() -> {
//                    binding.etFirstName.error = "Please enter your first name"
//                }
//
//                lastName.isEmpty() -> {
//                    binding.etLastName.error = "Please enter your last name"
//                }
//
//                phone.isEmpty() -> {
//                    binding.etMobile.error = "Please enter your mobile number"
//                }
//
//                else -> {
//                    if (firstName.length in 2..150) {
//                        if (lastName.length in 2..150) {
//                            if (isPhoneNumberValid(phone)) {
//                                updateProfile(firstName, lastName, phone)
//                            } else {
//                                binding.etMobile.error =
//                                    "Invalid mobile number format"
//                            }
//                        } else {
//                            binding.etLastName.error =
//                                "Last name must be a minimum of 2 characters and a maximum of 150 characters"
//                        }
//                    } else {
//                        binding.etFirstName.error =
//                            "First name must be a minimum of 2 characters and a maximum of 150 characters"
//                    }
//
//                }
//            }
//        }
    }

    private fun getLocation() {
        viewModel.getProvinces()
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, viewModel.provinceNames)
        val autoCompleteTextViewProvinsi = binding.autoCompleteTextViewProvinsi
        autoCompleteTextViewProvinsi.setAdapter(arrayAdapter)
        autoCompleteTextViewProvinsi.setOnItemClickListener { parent, view, position, id ->
            viewModel.setCity(true)
            val selectedProvinsi = parent.getItemAtPosition(position).toString()
            val codeProvince = viewModel.provinceDictionary.entries.firstOrNull { it.value == selectedProvinsi }?.key
            Toast.makeText(this, "Kode Provinsi yang dipilih: $codeProvince", Toast.LENGTH_SHORT).show()
            binding.dropdownKota.isEnabled = true
            viewModel.cityNames.clear()
            viewModel.getCity(codeProvince.toString())
            val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, viewModel.cityNames)
            val autoCompleteTextViewKota = binding.autoCompleteTextViewKota
            autoCompleteTextViewKota.setText("Pilih Kota")
            autoCompleteTextViewKota.setAdapter(arrayAdapter)
        }
    }

    private fun showCity(condition: Boolean) {
        binding.autoCompleteTextViewKota.isEnabled = condition
    }

    private fun showLoading(isLoading: Boolean) {
        binding.scrollViewProfile.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun isPhoneNumberValid(phoneNumber: String): Boolean {
        val pattern = Regex("^(8)\\d{9,}$")
        return pattern.matches(phoneNumber)
    }

    private fun getDataFromDB() {
        viewModel.getUserId().observe(this) { uid ->
            user_id = uid
        }
        viewModel.setLoading(false)
        viewModel.getFirstname().observe(this) { firstname ->
            binding.etFirstName.text = Editable.Factory.getInstance().newEditable(firstname)
        }
        viewModel.getLastname().observe(this) { lastname ->
            binding.etLastName.text = Editable.Factory.getInstance().newEditable(lastname)
        }
        viewModel.getEmail().observe(this) { email ->
            binding.etEmail.text = Editable.Factory.getInstance().newEditable(email)
            binding.etEmail.isFocusable = false
            binding.etEmail.isFocusableInTouchMode = false
            binding.etEmail.isCursorVisible = false
        }
        binding.etMobile.setText("81234567890")
//        binding.etMobile.text = Editable.Factory.getInstance().newEditable("81234567890")
        binding.autoCompleteTextViewProvinsi.text = Editable.Factory.getInstance().newEditable("Kalimantan Timur")
        binding.autoCompleteTextViewKota.text = Editable.Factory.getInstance().newEditable("Samarinda")
        binding.etAddress.text = Editable.Factory.getInstance().newEditable("Jalan Pangeran Pattimura No 47")
//        viewModel.user_id.observe(this) { user_id ->
//            binding.etAddress.text = Editable.Factory.getInstance().newEditable(user_id)
//        }
    }

    fun updateDataToDB() {
        val firstName = binding.etFirstName.text.toString()
        val lastName = binding.etLastName.text.toString()
        val mobile = binding.etMobile.text.toString()
        val state = binding.autoCompleteTextViewProvinsi.text.toString()
        val city = binding.autoCompleteTextViewKota.text.toString()
        val address = binding.etAddress.text.toString()
        Log.e("Profile", city)
        viewModel.getToken().observe(this) { token ->
            val tokens = "Bearer $token"
            viewModel.updateUser(tokens, firstName, lastName, mobile, user_id, address, city, state).observe(this) { result ->
                Log.e("Profile", result.toString())
                result?.let {
                    when (it) {
                        is Result.Loading -> {
                            Log.e("Profile", tokens)
                            Log.e("Profile", user_id)
                            showLoading(true)
                        }

                        is Result.Success -> {
                            showLoading(false)
                            Log.e("Profile", "OUT")
                            it.data.let { response ->
//                            response.loginData?.let { loginData ->
//                                setData(loginData)
//                                Log.e("IN", "IN")
//                            }

                                Log.e("Profile", "IN")
//                              coba di test dulu apakah firstname dan lastname nya berhasil di update atau tidak
                                val firstname = response.data?.firstname ?: ""
                                val lastname = response.data?.lastname ?: ""
                                viewModel.saveFirstname(firstname)
                                viewModel.saveLastname(lastname)

                                Toast.makeText(this, "Update Profile Successful", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                            }

                        }

                        is Result.Error -> {
                            showLoading(false)
                            Log.e("Profile", it.toString())
//                            Toast.makeText(this, "Update Profile Failed", Toast.LENGTH_SHORT).show()
//                            val intent = Intent(this, MainActivity::class.java)
//                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//                            startActivity(intent)
                        }
                    }
                }
        }


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

