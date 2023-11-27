package com.example.capstoneproject.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.capstoneproject.databinding.ActivityRegisterBinding
import com.example.capstoneproject.model.dataUser
import com.example.capstoneproject.ui.login.LoginActivity
import com.example.capstoneproject.ui.otp.OTPActivity
// import com.google.firebase.database.DataSnapshot
// import com.google.firebase.database.DatabaseError
// import com.google.firebase.database.DatabaseReference
// import com.google.firebase.database.FirebaseDatabase
// import com.google.firebase.database.ValueEventListener

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

        binding.loginhere.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnregis.setOnClickListener {
            val firstname = binding.firstname.text.toString()
            val lastname = binding.lastname.text.toString()
            val mobile = binding.mobile.text.toString()
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val confirmPassword = binding.passwordconfirm.text.toString()

            if (firstname.isNotEmpty() && lastname.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && mobile.isNotEmpty()) {
                if (password == confirmPassword) {
                    viewModel.regist(firstname, mobile, email, lastname, password)
                    /*databaseReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(!snapshot.exists()) {
                                var intent= Intent(this@RegisterActivity, OTPActivity::class.java)
                                intent.putExtra("firstname", firstname)
                                intent.putExtra("lastname", lastname)
                                intent.putExtra("email", email)
                                intent.putExtra("password", password)
                                startActivity(intent)
                                startActivity(Intent(this@RegisterActivity, OTPActivity::class.java))
                                finish()
                            } else {
                                Toast.makeText(this@RegisterActivity, "This account already exist", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(this@RegisterActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                        }
                    })*/
                    /*var intent= Intent(this@RegisterActivity, OTPActivity::class.java)
                    intent.putExtra("firstname", firstname)
                    intent.putExtra("lastname", lastname)
                    intent.putExtra("email", email)
                    intent.putExtra("password", password)
                    startActivity(intent)
                    regist(email)*/
                    /*regist(firstname, email, lastname, password)*/
                } else {
                    Toast.makeText(this, "The password confirm is not match! ", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Fill all the data!", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.registUser.observe(this, Observer { newUser ->
            if (newUser != null) {
                Toast.makeText(this@RegisterActivity, "SignUp Success", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                finish()
            } else {
                Toast.makeText(this@RegisterActivity, "Email already used!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    /*private fun regist(*//*firstname: String, *//*email: String*//*, lastname: String, password: String*//*) {
        databaseReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(!snapshot.exists()) {
                    *//*val id = databaseReference.push().key
                    val dataUser = dataUser(firstname, id, email, lastname, password)
                    databaseReference.child(id!!).setValue(dataUser)
                    Toast.makeText(this@RegisterActivity, "SignUp is success", Toast.LENGTH_SHORT).show()*//*
                    *//*startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    finish()*//*
                    startActivity(Intent(this@RegisterActivity, OTPActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@RegisterActivity, "This account already exist", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@RegisterActivity, RegisterActivity::class.java))
                    finish()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@RegisterActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }*/
}
