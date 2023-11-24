package com.example.capstoneproject.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.capstoneproject.databinding.ActivityRegisterBinding
import com.example.capstoneproject.model.dataUser
import com.example.capstoneproject.ui.login.LoginActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")

        binding.loginhere.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnregis.setOnClickListener {
            val firstname = binding.firstname.text.toString()
            val lastname = binding.lastname.text.toString()
            val email = binding.emailtext.text.toString()
            val password = binding.passwordtext.text.toString()

            if (firstname.isNotEmpty() && lastname.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                Log.e("firstname", firstname)
                Log.e("lastname", lastname)
                Log.e("email", email)
                Log.e("password", password)
                regist(firstname, email, lastname, password)
            } else {
                Toast.makeText(this, "Fill all the data!", Toast.LENGTH_SHORT).show()
            }

            /*if(binding.firstname.text.toString().isEmpty() || binding.lastname.text.toString().isEmpty() || binding.emailtext.text.toString().isEmpty() || binding.passwordtext.text.toString().isEmpty() || binding.passwordconfirm.text.toString().isEmpty()) {
                Toast.makeText(this, "Fill all the data!", Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(this, OTPActivity::class.java))
            }*/
        }
    }

    private fun regist(firstname: String, email: String, lastname: String, password: String) {
        databaseReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(!snapshot.exists()) {
                    val id = databaseReference.push().key
                    val dataUser = dataUser(firstname, id, email, lastname, password)
                    databaseReference.child(id!!).setValue(dataUser)
                    Toast.makeText(this@RegisterActivity, "SignUp is success", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@RegisterActivity, "This account already exist", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@RegisterActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
