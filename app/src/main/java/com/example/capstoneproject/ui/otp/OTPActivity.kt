package com.example.capstoneproject.ui.otp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.telephony.SmsManager
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.capstoneproject.databinding.ActivityOtpactivityBinding
import com.example.capstoneproject.ui.login.LoginActivity
import com.example.capstoneproject.ui.register.RegisterActivity
import com.example.capstoneproject.ui.register.RegisterViewModel
import kotlin.random.Random
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.database.ValueEventListener

class OTPActivity : AppCompatActivity() {

    companion object {
        const val MY_PERMISSIONS_REQUEST_SEND_SMS = 1
    }

    private lateinit var binding: ActivityOtpactivityBinding
    private val viewModel: RegisterViewModel by viewModels()
    /*private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference*/
    var firstname: String=""
    var lastname: String=""
    var mobile: String= ""
    var email: String=""
    var password: String=""
    val plan: String="none"
    var resendCounter: Int = 0
    val maxResendCount = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.SEND_SMS),
                MY_PERMISSIONS_REQUEST_SEND_SMS
            )
        }

        /*firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")*/
        /*FirebaseApp.initializeApp(this)*/

        firstname = intent.getStringExtra("firstname").toString()
        lastname = intent.getStringExtra("lastname").toString()
        email = intent.getStringExtra("email").toString()
        mobile = intent.getStringExtra("mobile").toString()
        password = intent.getStringExtra("password").toString()

        val otpCode = generateRandomOTP()
        sendOtpSms(mobile, otpCode)

        viewModel.registUser.observe(this, Observer { newUser ->
            if (newUser != null) {
                Toast.makeText(this@OTPActivity, "SignUp Success", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@OTPActivity, LoginActivity::class.java))
                finish()
            } else {
                Toast.makeText(this@OTPActivity, "Upss the email already used! SignUp failed", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@OTPActivity, RegisterActivity::class.java))
                finish()
            }
        })

        binding.btnverifydotp.setOnClickListener {
            if(binding.otp.text.toString().isEmpty()) {
                Toast.makeText(this@OTPActivity, "Enter the OTP code!", Toast.LENGTH_SHORT).show()
            } else {
                if (binding.otp.text.toString() == otpCode) {
                    Toast.makeText(this@OTPActivity, "Verification Success", Toast.LENGTH_SHORT).show()
                    viewModel.regist(firstname, mobile, email, lastname, password, plan)
                    Log.e("firstname", firstname)
                    Log.e("lastname", lastname)
                    Log.e("email", email)
                    Log.e("mobile", mobile)
                    Log.e("password", password)
                    Log.e("password", plan)
                } else {
                    Toast.makeText(this@OTPActivity, "Verification Failed", Toast.LENGTH_SHORT).show()
                }
                /*Log.e("firstname", firstname)
                Log.e("lastname", lastname)
                Log.e("email", email)
                Log.e("mobile", mobile)
                Log.e("password", password)
                *//*verifyOtp(email, password)*//*
                *//*regist(firstname, email, lastname, password)*//*
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)*/
            }
        }

        binding.resendotphere.setOnClickListener {
            if (resendCounter < maxResendCount) {

                resendCounter++
                val otpCode = generateRandomOTP()
                sendOtpSms(mobile, otpCode)
            } else {
                binding.resendotphere.visibility = View.INVISIBLE
                binding.resendText.visibility = View.INVISIBLE
            }
        }
    }

    private fun generateRandomOTP(): String {
        return Random.nextInt(1000, 9999).toString()
    }

    private fun sendOtpSms(phoneNumber: String, otpCode: String) {
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(phoneNumber, null, "Your OTP code is: $otpCode", null, null)
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
