package com.example.capstoneproject.ui.otp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpemailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth=FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        firstname = intent.getStringExtra("firstname").toString()
        lastname = intent.getStringExtra("lastname").toString()
        email = intent.getStringExtra("email").toString()
        password = intent.getStringExtra("password").toString()

        binding.signupText.visibility = View.INVISIBLE
        binding.reSendHere.visibility = View.INVISIBLE

        binding.btnverifydotp.setOnClickListener {
            val otp = binding.etOtp.text.toString()
            when {
                otp.isEmpty() -> {
                    binding.etOtp.error = "Please enter the OTP code!"
                }

                else -> {
                    if (otp.length !=6) {
                        binding.etOtp.error = "OTP code must be 6 numbers!"
                    } else {
                        if (!otp.equals(random.toString())) {
                            if (attemptsRemainingVerify > 1) {
                                Toast.makeText(
                                    this@OTPEmailActivity,
                                    "Incorrect OTP! Attempts remaining: ${attemptsRemainingVerify - 1}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                attemptsRemainingVerify--
                            } else {
                                disableVerifyOTP()
                                enableResend()
                            }
                        } else {
                            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                                if (it.isSuccessful) {
                                    Toast.makeText(
                                        this@OTPEmailActivity,
                                        "The code is Valid!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    userID = auth.currentUser!!.uid
                                    val plan = "None"
                                    val DocumentReference = firestore.collection("user").document(userID)
                                    val userData = hashMapOf(
                                        "uid" to userID,
                                        "firstname" to firstname,
                                        "lastname" to lastname,
                                        "email" to email,
                                        "password" to password,
                                        "plan" to plan
                                    )

                                    if (mobile.isNotEmpty()) {
                                        userData["mobile"] = mobile
                                    } else {
                                        userData["mobile"] = "" // Jika mobile kosong, simpan sebagai string kosong ("")
                                    }
                                    DocumentReference.set(userData).addOnSuccessListener {
                                        Log.e("SuccessRegist", "DocumentSnapshot added with ID: $userID")
                                        Toast.makeText(this, "Regist Success! ", Toast.LENGTH_SHORT).show()
                                    }
                                        .addOnFailureListener {
                                            Toast.makeText(this, "Regist Failed! ", Toast.LENGTH_SHORT).show()
                                        }
                                    var intent= Intent(this@OTPEmailActivity, LoginActivity::class.java)
                                    startActivity(intent)
                                } else {
                                    Toast.makeText(this@OTPEmailActivity, it.exception?.message.toString(), Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
            }
        }

        binding.btnBack.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        binding.reSendHere.setOnClickListener {
            if (attemptsRemainingResend > 0) {
                random()
                binding.reSendHere.isEnabled = true
                binding.signupText.visibility = View.INVISIBLE
                binding.reSendHere.visibility = View.INVISIBLE
                attemptsRemainingResend--
                enableVerifyOTP()
            } else {
                startResendCountdown()
            }
        }

        random()
        setFocusable()
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
