package com.example.capstoneproject.ui.otp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.capstoneproject.R
import com.example.capstoneproject.databinding.ActivityOtpemailBinding
import com.example.capstoneproject.ui.login.LoginActivity
import com.example.capstoneproject.ui.register.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.random.Random
import kotlin.random.nextInt
import papaya.`in`.sendmail.SendMail

class OTPEmailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpemailBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private var attemptsRemainingVerify = 3 // Jumlah percobaan verifikasi tersisa
    private var attemptsRemainingResend = 3 // Jumlah percobaan resend otp tersisa
    private var isCountdownActiveResend = false // Apakah countdown aktif
    private lateinit var countdownTimer: CountDownTimer
    private var countdownTimeLeft: Long = 0
    private var firstname: String = ""
    private var lastname: String = ""
    private var email: String = ""
    private var mobile: String = ""
    private var password: String = ""
    private var userID: String = ""
    private var random : Int = 0

    private var pinMap = mutableMapOf<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpemailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setPin()

        auth=FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        firstname = intent.getStringExtra("firstname").toString()
        lastname = intent.getStringExtra("lastname").toString()
        email = intent.getStringExtra("email").toString()
        password = intent.getStringExtra("password").toString()

        binding.signupText.visibility = View.INVISIBLE
        binding.reSendHere.visibility = View.INVISIBLE

        binding.btnverifydotp.setOnClickListener {
            val otp = pinMap.values.joinToString("")
            when {
                otp.isEmpty() -> {
                    Toast.makeText(this@OTPEmailActivity, "Code masih kosong", Toast.LENGTH_SHORT).show()
                } else -> {
                    Toast.makeText(this@OTPEmailActivity, otp, Toast.LENGTH_SHORT).show()
                }

//                else -> {
//                    if (otp.length !=6) {
//                        binding.etOtp.error = "OTP code must be 6 numbers!"
//                    } else {
//                        if (!otp.equals(random.toString())) {
//                            if (attemptsRemainingVerify > 1) {
//                                Toast.makeText(
//                                    this@OTPEmailActivity,
//                                    "Incorrect OTP! Attempts remaining: ${attemptsRemainingVerify - 1}",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                                attemptsRemainingVerify--
//                            } else {
//                                disableVerifyOTP()
//                                enableResend()
//                            }
//                        } else {
//                            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
//                                if (it.isSuccessful) {
//                                    Toast.makeText(
//                                        this@OTPEmailActivity,
//                                        "The code is Valid!",
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//                                    userID = auth.currentUser!!.uid
//                                    val plan = "None"
//                                    val DocumentReference = firestore.collection("user").document(userID)
//                                    val userData = hashMapOf(
//                                        "uid" to userID,
//                                        "firstname" to firstname,
//                                        "lastname" to lastname,
//                                        "email" to email,
//                                        "password" to password,
//                                        "plan" to plan
//                                    )
//
//                                    if (mobile.isNotEmpty()) {
//                                        userData["mobile"] = mobile
//                                    } else {
//                                        userData["mobile"] = "" // Jika mobile kosong, simpan sebagai string kosong ("")
//                                    }
//                                    DocumentReference.set(userData)
//                                        .addOnSuccessListener {
//                                            Log.e("SuccessRegist", "DocumentSnapshot added with ID: $userID")
//                                            Toast.makeText(this, "Register Successful", Toast.LENGTH_SHORT).show()
//                                        }
//                                        .addOnFailureListener {
//                                            Toast.makeText(this, "Register Failed", Toast.LENGTH_SHORT).show()
//                                        }
//                                    var intent= Intent(this@OTPEmailActivity, LoginActivity::class.java)
//                                    startActivity(intent)
//                                } else {
//                                    Log.d("OTPEmail", "Error in OTP Email Activity : ${it.exception?.message.toString()}")
//                                }
//                            }
//                        }
//                    }
//                }
            }
        }

        binding.btnBack.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

//        binding.reSendHere.setOnClickListener {
//            if (attemptsRemainingResend > 0) {
//                random()
//                binding.reSendHere.isEnabled = true
//                binding.signupText.visibility = View.INVISIBLE
//                binding.reSendHere.visibility = View.INVISIBLE
//                attemptsRemainingResend--
//                enableVerifyOTP()
//            } else {
//                startResendCountdown()
//            }
//        }

//        random()
//        setFocusable()
    }

    private fun disableVerifyOTP() {
        binding.btnverifydotp.isEnabled = false
        Toast.makeText(this@OTPEmailActivity, "Try re-send the code", Toast.LENGTH_SHORT).show()
    }

    private fun enableVerifyOTP() {
        binding.btnverifydotp.isEnabled = true
        attemptsRemainingVerify = 3
    }

    private fun startResendCountdown() {
        if (!isCountdownActiveResend) {
            countdownTimer = object : CountDownTimer(60000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    countdownTimeLeft = millisUntilFinished // Simpan waktu yang tersisa
                    Toast.makeText(
                        this@OTPEmailActivity,
                        "Try again in ${countdownTimeLeft / 1000} seconds",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                override fun onFinish() {
                    enableResend()
                    attemptsRemainingResend = 3
                }
            }.start()

            isCountdownActiveResend = true
        }
    }

    private fun enableResend() {
        binding.reSendHere.isEnabled = true
        binding.signupText.visibility = View.VISIBLE
        binding.reSendHere.visibility = View.VISIBLE
        isCountdownActiveResend = false
    }

    fun random(){
        random = Random.nextInt(100000..999999)
        var mail = SendMail("dindhana12@gmail.com","kcdjckbwuwznuvin",email,"Wownet OTP Code",
            "Here is your OTP is -> $random \n" +
                    "Please ignore if you didn't ask the code")
        mail.execute()
    }

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

    private fun setPin() {
        val pinList = listOf(binding.pin1, binding.pin2, binding.pin3, binding.pin4, binding.pin5, binding.pin6)
        for (i in pinList.indices) {
            var currentPin = pinList[i]
            val nextPin = if (i == pinList.size-1) pinList[i] else pinList[i + 1]
            Log.d("Home", i.toString())

            currentPin.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    if (!s.isNullOrEmpty() && s.length == 1) {
                        pinList[i].background = ContextCompat.getDrawable(this@OTPEmailActivity, R.drawable.border_green)
                        nextPin.requestFocus()
                        pinMap[i.toString()] = pinList[i].text.toString()
                    } else if (i == 0) {
                        pinList[i].background = ContextCompat.getDrawable(this@OTPEmailActivity, R.drawable.border_default)
                        pinList[i].requestFocus()
                        pinMap[i.toString()] = ""
                    } else {
                        pinList[i].background = ContextCompat.getDrawable(this@OTPEmailActivity, R.drawable.border_default)
                        pinList[i-1].requestFocus()
                        pinMap[i.toString()] = ""
                    }
//                    Toast.makeText(this@MainActivity, "Pin ke-$i telah di Click", Toast.LENGTH_SHORT).show()
//                    Toast.makeText(this@OTPEmailActivity, "Code ${pinMap.values.joinToString("")}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
