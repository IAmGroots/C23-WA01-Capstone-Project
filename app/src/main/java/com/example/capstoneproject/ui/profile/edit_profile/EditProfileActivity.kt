package com.example.capstoneproject.ui.profile.edit_profile

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.capstoneproject.MainActivity
import com.example.capstoneproject.R
import com.example.capstoneproject.data.response.UserProfile
import com.example.capstoneproject.data.result.Result
import com.example.capstoneproject.databinding.ActivityEditProfileBinding
import com.example.capstoneproject.preferences.ViewModelFactory

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var viewModel: EditProfileViewModel

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
            val firstName = binding.etFirstName.text.toString()
            val lastName = binding.etLastName.text.toString()
            val phone = binding.etMobile.text.toString()

            when {
                firstName.isEmpty() -> { binding.etFirstName.error = "Please enter your first name" }
                lastName.isEmpty() -> { binding.etLastName.error = "Please enter your last name" }
                phone.isEmpty() -> { binding.etMobile.error = "Please enter your mobile number" }
                else -> {
                    if (firstName.length in 2..150) {
                        if (lastName.length in 2..150) {
                            if (isPhoneNumberValid(phone)) {
                                updateDataToDB()
                            } else {
                                binding.etMobile.error = "Invalid mobile number format"
                            }
                        } else {
                            binding.etLastName.error = "Last name must be a minimum of 2 characters and a maximum of 150 characters"
                        }
                    } else {
                        binding.etFirstName.error = "First name must be a minimum of 2 characters and a maximum of 150 characters"
                    }
                }
            }
        }
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
//            Toast.makeText(this, "Kode Provinsi yang dipilih: $codeProvince", Toast.LENGTH_SHORT).show()
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
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun isPhoneNumberValid(phoneNumber: String): Boolean {
        val pattern = Regex("^(8)\\d{9,}$")
        return pattern.matches(phoneNumber)
    }

    private fun getDataFromDB() {
        viewModel.setLoading(false)
        viewModel.userProfile.observe(this) { profile ->
            binding.etFirstName.text = Editable.Factory.getInstance().newEditable(profile.firstName)
            binding.etLastName.text = Editable.Factory.getInstance().newEditable(profile.lastName)
            binding.etEmail.text = Editable.Factory.getInstance().newEditable(profile.email)
            binding.etMobile.text = Editable.Factory.getInstance().newEditable(profile.mobile.substring(1))
//            binding.autoCompleteTextViewProvinsi.text = Editable.Factory.getInstance().newEditable(profile.state)
//            binding.autoCompleteTextViewKota.text = Editable.Factory.getInstance().newEditable(profile.city)
            binding.etAddress.text = Editable.Factory.getInstance().newEditable(profile.address)

            // Set nilai awal untuk AutoCompleteTextView dari database
            val initialProvinsi = profile.state
            val initialKota = profile.city

            binding.autoCompleteTextViewProvinsi.setText(initialProvinsi, false)
            binding.autoCompleteTextViewKota.setText(initialKota, false)

            viewModel.getProvinces()
            val arrayAdapterProvinsi = ArrayAdapter(this, R.layout.dropdown_item, viewModel.provinceNames)
            binding.autoCompleteTextViewProvinsi.setAdapter(arrayAdapterProvinsi)
            val arrayAdapterKota = ArrayAdapter(this, R.layout.dropdown_item, viewModel.cityNames)
            binding.autoCompleteTextViewKota.setAdapter(arrayAdapterKota)
        }
    }

    fun updateDataToDB() {
        val firstName = binding.etFirstName.text.toString()
        val lastName = binding.etLastName.text.toString()
        val mobile = "0" + binding.etMobile.text.toString()
        val state = binding.autoCompleteTextViewProvinsi.text.toString()
        val city = binding.autoCompleteTextViewKota.text.toString()
        val address = binding.etAddress.text.toString()
        Log.e("profile", "Profile : ${firstName} - ${lastName} - ${mobile} - ${state} - ${city} - ${address}")
        viewModel.userProfile.observe(this) { profile ->
            val tokens = "Bearer ${profile.token}"
            viewModel.updateUser(tokens, firstName, lastName, mobile, address, city, state).observe(this) { result ->
                Log.e("Profile", result.toString())
                when (result) {
                    is Result.Loading -> {
                        Log.e("Profile", "Loading")
                        viewModel.setLoading(true)
                    }

                    is Result.Success -> {
                        viewModel.setLoading(false)
                        Log.e("Profile", "OUT")
                        val newProfile = result.data
                        Log.d("profile", "New Profile : ${newProfile}")

                        val token = profile.token
                        val uid = profile.uid
                        val email = profile.email

                        val profile = UserProfile(
                            token = token,
                            uid = uid,
                            firstName = firstName,
                            lastName = lastName,
                            email = email,
                            mobile = mobile,
                            state = state,
                            city = city,
                            address = address,
                        )
                        viewModel.saveProfile(profile)
                        Toast.makeText(this, "Update Profile Successful", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        intent.putExtra("navigateToProfile", true)
                        startActivity(intent)
                    }

                    is Result.Error -> {
                        viewModel.setLoading(false)
                        Toast.makeText(this, "Terjadi Error", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        Log.d("profile", "Error : ${result.error}")
                    }
                }
            }
        }
    }
}

