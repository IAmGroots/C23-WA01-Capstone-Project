package com.example.capstoneproject.ui.otp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Toast
import android.util.Log
import android.view.MotionEvent
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.capstoneproject.MainActivity
import com.example.capstoneproject.databinding.ActivityOtpmobileBinding
import com.example.capstoneproject.ui.login.LoginActivity
import com.example.capstoneproject.ui.register.RegisterActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class OTPMobileActivity : AppCompatActivity() {
    companion object {
        const val MY_PERMISSIONS_REQUEST_SEND_SMS = 1
    }

    private lateinit var binding: ActivityOtpmobileBinding
    private val viewModel: OTPViewModel by viewModels()
    /*private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference*/
    private val db = Firebase.firestore
    private val userID = FirebaseAuth.getInstance().currentUser!!.uid
    private var firstname = ""
    private var lastname = ""
    private var mobile = ""
    var resendCounter = 0
    var sendAttemp = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpmobileBinding.inflate(layoutInflater)
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

        firstname = intent.getStringExtra("firstname").toString()
        lastname = intent.getStringExtra("lastname").toString()
        mobile = intent.getStringExtra("mobile").toString()

        /*firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")*/
        /*FirebaseApp.initializeApp(this)*/

//        binding.resendotphere.visibility = View.INVISIBLE
//        binding.resendText.visibility = View.INVISIBLE

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

        setFocusable()

        val otpCode = generateRandomOTP()
        sendOtpSms(mobile, otpCode)

//        viewModel.registUser.observe(this, Observer { newUser ->
//            if (newUser != null) {
//                Toast.makeText(this@OTPActivity, "SignUp Success", Toast.LENGTH_SHORT).show()
//                startActivity(Intent(this@OTPActivity, LoginActivity::class.java))
//                finish()
//            } else {
//                Toast.makeText(this@OTPActivity, "Upss the email already used! SignUp failed", Toast.LENGTH_SHORT).show()
//                startActivity(Intent(this@OTPActivity, RegisterActivity::class.java))
//                finish()
//            }
//        })

        binding.btnverifydotp.setOnClickListener {
            if(binding.etOtp.text.toString().isEmpty()) {
                Toast.makeText(this@OTPMobileActivity, "Enter the OTP code!", Toast.LENGTH_SHORT).show()
            } else {
                if (binding.etOtp.text.toString() == otpCode) {
                    db.collection("user").whereEqualTo("uid", userID).limit(1).get()
                        .addOnSuccessListener { data ->
                            if (!data.isEmpty) {
                                val user = data.documents[0]
                                Log.d(
                                    "FIREBASE", "ID DOCUMENT : ${user.id} | ID USER : ${user.get("uid")}"
                                )

                                val sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)
                                val editor = sharedPreferences.edit()

                                editor.putString("firstName", firstname)
                                editor.putString("lastName", lastname)
                                editor.putString("phone", mobile)

                                editor.apply()

                                updateDataSourceUser(user.id, firstname, lastname, mobile)
                                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, MainActivity::class.java))
                            } else {
                                Log.d("FIREBASE", "Something went wrong")
                            }
                        }
                    Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                } else {
//                    sendAttemp++
                    Toast.makeText(this, "Profile failed to update", Toast.LENGTH_SHORT).show()
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

        binding.reSendhere.setOnClickListener {
//            if (resendCounter <= 3) {
//
//                resendCounter++
//                sendAttemp = 0
//                val otpCode = generateRandomOTP()
//                sendOtpSms(mobile, otpCode)
//                binding.reSendhere.visibility = View.INVISIBLE
//                binding.resendText.visibility = View.INVISIBLE
//            } else {
//                Toast.makeText(this@OTPActivity, "You have failed three times!", Toast.LENGTH_SHORT).show()
//                startActivity(Intent(this@OTPActivity, RegisterActivity::class.java))
//                finish()
//            }
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

    private fun generateRandomOTP(): String {
        return Random.nextInt(100000, 999999).toString()
    }

    private fun sendOtpSms(phoneNumber: String, otpCode: String) {
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(phoneNumber, null, "Your OTP code is: $otpCode", null, null)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setFocusable() {
        binding.etOtp.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    binding.etOtp.isFocusable = true
                    binding.etOtp.isFocusableInTouchMode = true
                }

                MotionEvent.ACTION_UP -> {
                    binding.etOtp.requestFocus()
                }
            }
            false
        }
    }

}
//    private lateinit var binding: ActivityOtpmobileBinding
//    private lateinit var auth: FirebaseAuth
//    private var verification: String? = null
//    private var firstname: String = ""
//    private var lastname: String = ""
//    private var mobile: String = ""
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityOtpmobileBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        auth = FirebaseAuth.getInstance()
//
//        firstname = intent.getStringExtra("firstname").toString()
//        lastname = intent.getStringExtra("lastname").toString()
//        mobile = intent.getStringExtra("mobile").toString()
//        sendOTP(mobile)
//
//        binding.btnverifydotp.setOnClickListener {
//            val otp = binding.etOtp.text.toString()
//            if (otp.isEmpty()) {
//                Toast.makeText(
//                    this@OTPMobileActivity,
//                    "Enter the OTP code!",
//                    Toast.LENGTH_SHORT
//                ).show()
//            } else {
//                verify(otp)
//                saveEmail()
//            }
//        }
//
//        binding.reSendhere.setOnClickListener {
//            sendOTP(mobile)
//        }
//
//        setFocusable()
//    }
//
//    private fun sendOTP(mobile : String) {
//        val options: PhoneAuthOptions = PhoneAuthOptions.newBuilder(auth)
//            .setPhoneNumber("+62$mobile")
//            .setTimeout(60L, TimeUnit.SECONDS)
//            .setActivity(this)
//            .setCallbacks(callbacks)
//            .build()
//        Log.e("phone number", "+62$mobile")
//        PhoneAuthProvider.verifyPhoneNumber(options)
//    }
//
//    private fun verify(codeOTP: String) {
//        val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(verification!!, codeOTP)
//        regist(credential)
//    }
//
//    private fun regist(credential: PhoneAuthCredential) {
//        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
//        firebaseAuth.signInWithCredential(credential)
//            .addOnCompleteListener(OnCompleteListener<AuthResult?> { task ->
//                if (task.isSuccessful) {
//                    saveEmail()
//                    Toast.makeText(this@OTPMobileActivity, "Verification Success", Toast.LENGTH_SHORT).show()
//                    startActivity(Intent(this@OTPMobileActivity, LoginActivity::class.java))
//                    finish()
//                }
//            })
//    }
//
//    private val callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
//        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
//            }
//
//            override fun onVerificationFailed(e: FirebaseException) {
//                Toast.makeText(this@OTPMobileActivity, "Verification Failed", Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onCodeSent(input: String, token: PhoneAuthProvider.ForceResendingToken) {
//                super.onCodeSent(input, token)
//                verification = input
//                Toast.makeText(this@OTPMobileActivity, "Send OTP code...", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//    private fun saveEmail() {
//        val user = hashMapOf(
//            "firstname" to firstname,
//            "lastname" to lastname,
//            "mobile" to mobile
//        )
//        val db = Firebase.firestore
//        db.collection("users")
//            .add(user)
//            .addOnSuccessListener { documentReference ->
//                Log.e("SuccessRegist", "DocumentSnapshot added with ID: ${documentReference.id}")
//                Toast.makeText(this, "Regist Success! ", Toast.LENGTH_SHORT).show()
//            }
//            .addOnFailureListener { e ->
//                Log.e("FailRegist", "Error adding document", e)
//                Toast.makeText(this, "Regist Failed! ", Toast.LENGTH_SHORT).show()
//            }
//    }
//
//    @SuppressLint("ClickableViewAccessibility")
//    private fun setFocusable() {
//        binding.etOtp.setOnTouchListener { _, event ->
//            when (event.action) {
//                MotionEvent.ACTION_DOWN -> {
//                    binding.etOtp.isFocusable = true
//                    binding.etOtp.isFocusableInTouchMode = true
//                }
//
//                MotionEvent.ACTION_UP -> {
//                    binding.etOtp.requestFocus()
//                }
//            }
//            false
//        }
//    }
//}
