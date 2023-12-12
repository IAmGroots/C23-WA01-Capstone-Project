package com.example.capstoneproject.ui.profile.edit_profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.capstoneproject.MainActivity
import com.example.capstoneproject.databinding.ActivityEditProfileBinding
import com.example.capstoneproject.ui.chat.ChatActivity
import com.example.capstoneproject.ui.otp.OTPEmailActivity
import com.example.capstoneproject.ui.otp.OTPMobileActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private var db = Firebase.firestore
    private val userID = FirebaseAuth.getInstance().currentUser!!.uid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFocusable()

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
                                val sharedPreferences =
                                    getSharedPreferences("UserData", Context.MODE_PRIVATE)
                                val oldPhone = sharedPreferences.getString("phone", "")
                                if (oldPhone == phone) {
                                    updateProfile(firstName, lastName, phone)
                                } else {
                                    redirectToOTP(firstName, lastName, phone)
                                }
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
        loadUserDataFromSharedPreferences()
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

                    val sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()

                    editor.putString("firstName", firstName)
                    editor.putString("lastName", lastName)
                    editor.putString("phone", phone)

                    editor.apply()

                    updateDataSourceUser(user.id, firstName, lastName, phone)
                    Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    Toast.makeText(this, "Profile failed to update", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun redirectToOTP(firstName: String, lastName: String, phone: String) {
        val firestore = FirebaseFirestore.getInstance()
        val usersRef = firestore.collection("user")
        val query = usersRef.whereEqualTo("mobile", phone)

        query.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val documents = task.result
//                if (documents != null && !documents.isEmpty) {
//                    // Email sudah digunakan
//                    Toast.makeText(
//                        this@EditProfileActivity,
//                        "Mobile number is already in use!",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                } else {
                // Email belum digunakan, lanjutkan ke verifikasi OTP
                val intent = Intent(
                    this@EditProfileActivity, OTPMobileActivity::class.java
                )
                intent.putExtra("firstname", firstName)
                intent.putExtra("lastname", lastName)
                intent.putExtra("mobile", phone)
                startActivity(intent)
//                }
            } else {
                // Gagal melakukan pengecekan
                Toast.makeText(
                    this@EditProfileActivity,
                    "Failed to check mobile number existence!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    private fun loadUserDataFromSharedPreferences() {
        val sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val firstName = sharedPreferences.getString("firstName", "")
        val lastName = sharedPreferences.getString("lastName", "")
        val phone = sharedPreferences.getString("phone", "")

        // Menampilkan informasi pengguna di EditText pada profil
        binding.etFirstName.setText(firstName)
        binding.etLastName.setText(lastName)

        if (phone == null) {
            binding.etMobile.setText("")
        } else {
            binding.etMobile.setText(phone)
        }
    }

    private fun updateDataSourceUser(
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

