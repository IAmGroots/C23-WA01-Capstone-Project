package com.example.capstoneproject.ui.otp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.capstoneproject.databinding.ActivityOtpactivityBinding
import com.example.capstoneproject.model.dataUser
import com.example.capstoneproject.ui.login.LoginActivity
// import com.google.firebase.database.DataSnapshot
// import com.google.firebase.database.DatabaseError
// import com.google.firebase.database.DatabaseReference
// import com.google.firebase.database.FirebaseDatabase
// import com.google.firebase.database.ValueEventListener

class OTPActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOtpactivityBinding
    // private lateinit var firebaseDatabase: FirebaseDatabase
    // private lateinit var databaseReference: DatabaseReference
    var firstname: String=""
    var lastname: String=""
    var email: String=""
    var password: String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //firebaseDatabase = FirebaseDatabase.getInstance()
        //databaseReference = firebaseDatabase.reference.child("users")

        firstname = intent.getStringExtra("firstname").toString()
        lastname = intent.getStringExtra("lastname").toString()
        email = intent.getStringExtra("email").toString()
        password = intent.getStringExtra("password").toString()

        binding.btnverifydotp.setOnClickListener {
            if(binding.otp.text.toString().isEmpty()) {
                Toast.makeText(this@OTPActivity, "Enter the OTP code!", Toast.LENGTH_SHORT).show()
            } else {
                Log.e("firstname", firstname)
                Log.e("lastname", lastname)
                Log.e("email", email)
                Log.e("password", password)
                /*regist(firstname, email, lastname, password)*/
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }
    }

    /*private fun regist(firstname: String, mobile: String, email: String, lastname: String, password: String) {
        databaseReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    val id = databaseReference.push().key
                    val dataUser = dataUser(firstname, mobile, id, email, lastname, password)
                    databaseReference.child(id!!).setValue(dataUser)
                    Toast.makeText(this@OTPActivity, "SignUp is success", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@OTPActivity, LoginActivity::class.java))
                    finish()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@OTPActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }*/
}
