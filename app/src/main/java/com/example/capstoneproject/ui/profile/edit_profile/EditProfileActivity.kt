package com.example.capstoneproject.ui.profile.edit_profile

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.capstoneproject.MainActivity
import com.example.capstoneproject.databinding.ActivityEditProfileBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var viewModel: EditProfileViewModel
    private var db = Firebase.firestore
    private val userID = FirebaseAuth.getInstance().currentUser!!.uid
    private lateinit var oldPhone: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[EditProfileViewModel::class.java]

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        setFocusable()
        getDataFromDB(userID)

        binding.btnCancel.setOnClickListener {
            super.onBackPressed()
        }
        binding.btnSaveChanges.setOnClickListener {
            val firstName = binding.etFirstName.text.toString()
            val lastName = binding.etLastName.text.toString()
            val phone = binding.etMobile.text.toString()

            when {
                firstName.isEmpty() -> {
                    binding.etFirstName.error = "Please enter your first name"
                }

                lastName.isEmpty() -> {
                    binding.etLastName.error = "Please enter your last name"
                }

                phone.isEmpty() -> {
                    binding.etMobile.error = "Please enter your mobile number"
                }

                else -> {
                    if (firstName.length in 2..150) {
                        if (lastName.length in 2..150) {
                            if (isPhoneNumberValid(phone)) {
                                updateProfile(firstName, lastName, phone)
                            } else {
                                binding.etMobile.error =
                                    "Invalid mobile number format"
                            }
                        } else {
                            binding.etLastName.error =
                                "Last name must be a minimum of 2 characters and a maximum of 150 characters"
                        }
                    } else {
                        binding.etFirstName.error =
                            "First name must be a minimum of 2 characters and a maximum of 150 characters"
                    }

                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.scrollViewProfile.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun isPhoneNumberValid(phoneNumber: String): Boolean {
        val pattern = Regex("^(8)\\d{9,}$")
        return pattern.matches(phoneNumber)
    }

    private fun updateProfile(firstName: String, lastName: String, phone: String) {
        db.collection("user").whereEqualTo("uid", userID).limit(1).get()
            .addOnSuccessListener { data ->
                if (!data.isEmpty) {
                    val user = data.documents[0]
                    Log.d(
                        "FIREBASE", "ID DOCUMENT : ${user.id} | ID USER : ${user.get("uid")}"
                    )

                    updateDataToDB(user.id, firstName, lastName, phone)
                    Toast.makeText(this, "Update Profile Successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Update Profile Unsuccessful", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun getDataFromDB(uid: String) {
        viewModel.setLoading(true)
        db.collection("user")
            .whereEqualTo("uid", uid)
            .limit(1)
            .get()
            .addOnSuccessListener { data ->
                if (!data.isEmpty) {
                    val user = data.documents[0]
                    oldPhone = user.get("mobile").toString()
                    binding.etFirstName.text = Editable.Factory.getInstance().newEditable(user.get("firstname").toString())
                    binding.etLastName.text = Editable.Factory.getInstance().newEditable(user.get("lastname").toString())
                    binding.etMobile.text = Editable.Factory.getInstance().newEditable(user.get("mobile").toString())
                }
                viewModel.setLoading(false)
            }
    }

    private fun updateDataToDB(
        userId: String, firstName: String, lastName: String, phone: String
    ) {
        // Dapatkan referensi ke dokumen pengguna di Firestore
        val db = FirebaseFirestore.getInstance()
        val userRef = db.collection("user").document(userId)

        // Update data pengguna di Firestore
        val updates = hashMapOf(
            "firstname" to firstName, "lastname" to lastName, "mobile" to phone
        )

        userRef.update(updates as Map<String, Any>).addOnSuccessListener {
            Log.d("UpdateFirestore", "DocumentSnapshot successfully updated!")
        }.addOnFailureListener { e ->
            Log.w("UpdateFirestore", "Error updating document", e)
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

