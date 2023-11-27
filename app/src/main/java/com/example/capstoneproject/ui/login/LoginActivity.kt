package com.example.capstoneproject.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.capstoneproject.MainActivity
import com.example.capstoneproject.databinding.ActivityLoginBinding
import com.example.capstoneproject.preferences.SettingPreferences
import com.example.capstoneproject.preferences.ViewModelFactory
import com.example.capstoneproject.preferences.dataStore
import com.example.capstoneproject.ui.register.RegisterActivity
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
//    private lateinit var firebaseDatabase: FirebaseDatabase
//    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val preferences = SettingPreferences.getInstance(application.dataStore)
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(preferences)
        )[LoginViewModel::class.java]

//        firebaseDatabase = FirebaseDatabase.getInstance()
//        databaseReference = firebaseDatabase.reference.child("users")

        viewModel.getEmail().observe(this) { email ->
            if (email == "empty") {
                binding.email.text = Editable.Factory.getInstance().newEditable("")
            } else {
                binding.email.text = Editable.Factory.getInstance().newEditable(email)
            }
        }

        binding.btnlogin.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                if (password.length >= 8) {
                    Log.e("email", email)
                    Log.e("password", password)
                    /*login(email, password)*/
                    viewModel.login(email, password)
                } else {
                    Toast.makeText(this, "Please check your password!", Toast.LENGTH_SHORT).show()
                }
            } else {
            }
        }

        viewModel.loginUser.observe(this) { userLogin ->
            if (userLogin != null) {
                Toast.makeText(this@LoginActivity, "SignIn is success", Toast.LENGTH_SHORT).show()
                MainActivity.isLogin = true
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                /*startActivity(Intent(this@LoginActivity, HomeFragment::class.java))
                finish()*/
            } else {
                Toast.makeText(this@LoginActivity, "SignIn is failed", Toast.LENGTH_SHORT).show()
            }
        }

        binding.regishere.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    /*private fun login(email: String, password: String) {
        databaseReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val dataUser = userSnapshot.getValue(dataUser::class.java)

                        if(dataUser != null && dataUser.password == password) {
                            Toast.makeText(this@LoginActivity, "SignIn is success", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@LoginActivity, HomeFragment::class.java))
                            finish()
                            return
                        }
                    }
                }
                Toast.makeText(this@LoginActivity, "SignIn is failed", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@LoginActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }*/
}
