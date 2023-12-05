package com.example.capstoneproject.ui.otp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.util.Log
import android.view.MotionEvent
import com.example.capstoneproject.databinding.ActivityOtpmobileBinding
import com.example.capstoneproject.ui.login.LoginActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.firestore
import java.util.concurrent.TimeUnit
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.database.ValueEventListener

class OTPMobileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOtpmobileBinding
    private lateinit var auth: FirebaseAuth
    private var verification: String? = null
    var firstname: String = ""
    var lastname: String = ""
    var mobile: String = ""
    var email: String = ""
    var password: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpmobileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        firstname = intent.getStringExtra("firstname").toString()
        lastname = intent.getStringExtra("lastname").toString()
        email = intent.getStringExtra("email").toString()
        mobile = intent.getStringExtra("mobile").toString()
        password = intent.getStringExtra("password").toString()
        sendOTP(mobile)

        binding.btnverifydotp.setOnClickListener {
            val otp = binding.etOtp.text.toString()
            if (otp.isEmpty()) {
                Toast.makeText(
                    this@OTPMobileActivity,
                    "Enter the OTP code!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                verify(otp)
                saveEmail()
            }
        }

        binding.reSendhere.setOnClickListener {
            sendOTP(mobile)
        }

        setFocusable()
    }

    private fun sendOTP(mobile : String) {
        val options: PhoneAuthOptions = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+62$mobile")
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()
        Log.e("phone number", "+62$mobile")
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun verify(codeOTP: String) {
        val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(verification!!, codeOTP)
        regist(credential)
    }

    private fun regist(credential: PhoneAuthCredential) {
        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(OnCompleteListener<AuthResult?> { task ->
                if (task.isSuccessful) {
                    saveEmail()
                    Toast.makeText(this@OTPMobileActivity, "Verification Success", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@OTPMobileActivity, LoginActivity::class.java))
                    finish()
                }
            })
    }

    private val callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(this@OTPMobileActivity, "Verification Failed", Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(input: String, token: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(input, token)
                verification = input
                Toast.makeText(this@OTPMobileActivity, "Send OTP code...", Toast.LENGTH_SHORT).show()
            }
        }

    private fun saveEmail() {
        val user = hashMapOf(
            "firstname" to firstname,
            "lastname" to lastname,
            "email" to email,
            "mobile" to mobile,
            "password" to password
        )
        val db = Firebase.firestore
        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.e("SuccessRegist", "DocumentSnapshot added with ID: ${documentReference.id}")
                Toast.makeText(this, "Regist Success! ", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.e("FailRegist", "Error adding document", e)
                Toast.makeText(this, "Regist Failed! ", Toast.LENGTH_SHORT).show()
            }

        /*auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userUpdateProfile = userProfileChangeRequest {
                        displayName = firstname
                        displayName = lastname
                        displayName = mobile
                    }
                    val userIsLogin = task.result.user
                    userIsLogin!!.updateProfile(userUpdateProfile)
                        .addOnCompleteListener {
                            *//*Toast.makeText(this@OTPActivity, "Verification Success", Toast.LENGTH_SHORT).show()*//*
                            startActivity(Intent(this@OTPActivity, LoginActivity::class.java))
                            finish()
                        }
                        .addOnFailureListener {errorProfile ->
                            Toast.makeText(this, errorProfile.localizedMessage, Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener {error ->
                Toast.makeText(this, error.localizedMessage, Toast.LENGTH_SHORT).show()
            }*/
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

/*companion object {
    const val MY_PERMISSIONS_REQUEST_SEND_SMS = 1
}

private lateinit var binding: ActivityOtpmobileactivityBinding
private val viewModel: OTPViewModel by viewModels()
*//*private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference*//*
    var firstname: String=""
    var lastname: String=""
    var mobile: String= ""
    var email: String=""
    var password: String=""
    val plan: String="none"
    var resendCounter = 0
    var sendAttemp = 0


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

        *//*firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")*//*
        *//*FirebaseApp.initializeApp(this)*//*

        firstname = intent.getStringExtra("firstname").toString()
        lastname = intent.getStringExtra("lastname").toString()
        email = intent.getStringExtra("email").toString()
        mobile = intent.getStringExtra("mobile").toString()
        password = intent.getStringExtra("password").toString()
        binding.resendotphere.visibility = View.INVISIBLE

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

        val otpCode = generateRandomOTP()
        sendOtpSms(mobile, otpCode)

        viewModel.registUser.observe(this, Observer { newUser ->
            if (newUser != null) {
                Toast.makeText(this@OTPMobileActivity, "SignUp Success", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@OTPMobileActivity, LoginActivity::class.java))
                finish()
            } else {
                Toast.makeText(this@OTPMobileActivity, "Upss the email already used! SignUp failed", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@OTPMobileActivity, RegisterActivity::class.java))
                finish()
            }
        })

        binding.btnverifydotp.setOnClickListener {
            if(binding.otp1.text.toString().isEmpty()) {
                Toast.makeText(this@OTPMobileActivity, "Enter the OTP code!", Toast.LENGTH_SHORT).show()
            } else {
                if (binding.otp1.text.toString() == otpCode) {
                    Toast.makeText(this@OTPMobileActivity, "Verification Success", Toast.LENGTH_SHORT).show()
                    viewModel.regist(firstname, mobile, email, lastname, password, plan)
                    Log.e("firstname", firstname)
                    Log.e("lastname", lastname)
                    Log.e("email", email)
                    Log.e("mobile", mobile)
                    Log.e("password", password)
                    Log.e("plan", plan)
                } else {
                    sendAttemp++
                    Toast.makeText(this@OTPMobileActivity, "Verification Failed", Toast.LENGTH_SHORT).show()
                    if (sendAttemp >= 3 && sendAttemp < 4) {
                        binding.resendotphere.visibility = View.VISIBLE
                    }
                }
                *//*Log.e("firstname", firstname)
                Log.e("lastname", lastname)
                Log.e("email", email)
                Log.e("mobile", mobile)
                Log.e("password", password)
                *//**//*verifyOtp(email, password)*//**//*
                *//**//*regist(firstname, email, lastname, password)*//**//*
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)*//*
            }
        }

        binding.resendotphere.setOnClickListener {
            if (resendCounter <= 3) {

                resendCounter++
                sendAttemp = 0
                val otpCode = generateRandomOTP()
                sendOtpSms(mobile, otpCode)
                binding.resendotphere.visibility = View.INVISIBLE
            } else {
                Toast.makeText(this@OTPMobileActivity, "You have failed three times!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@OTPMobileActivity, RegisterActivity::class.java))
                finish()
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

    *//*private fun regist(firstname: String, mobile: String, email: String, lastname: String, password: String) {
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
    }*//*
}
*/