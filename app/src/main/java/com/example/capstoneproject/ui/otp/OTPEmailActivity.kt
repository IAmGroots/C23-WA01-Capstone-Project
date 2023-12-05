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
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlin.random.Random
import kotlin.random.nextInt
import papaya.`in`.sendmail.SendMail

class OTPEmailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpemailBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private var attemptsRemainingVerify = 3 // Jumlah percobaan tersisa
    private var isCountdownActiveVerify = false // Apakah countdown aktif
    private var attemptsRemainingResend = 3
    private var isCountdownActiveResend = false
    private lateinit var countdownTimer: CountDownTimer
    private var countdownTimeLeft: Long = 0
    var firstname: String = ""
    var lastname: String = ""
    var mobile: String = ""
    var email: String = ""
    var password: String = ""
    var userID: String = ""
    var random : Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpemailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth=FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        /*if (auth.currentUser != null) {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }*/

        firstname = intent.getStringExtra("firstname").toString()
        lastname = intent.getStringExtra("lastname").toString()
        email = intent.getStringExtra("email").toString()
        mobile = intent.getStringExtra("mobile").toString()
        password = intent.getStringExtra("password").toString()

        binding.showEmail.setText(email.toString())
        binding.signupText.visibility = View.INVISIBLE
        binding.reSendHere.visibility = View.INVISIBLE

        binding.reSendHere.setOnClickListener {
            random()
        }

        binding.btnverifydotp.setOnClickListener {
            val otp = binding.etOtp.text.toString()
            if (otp.isEmpty()) {
                Toast.makeText(
                    this@OTPEmailActivity,
                    "Enter the OTP code!",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (!otp.equals(random.toString())){
                attemptsRemainingVerify--
                if (attemptsRemainingVerify == 0 && !isCountdownActiveVerify) {
                    Toast.makeText(
                        this@OTPEmailActivity,
                        "OTP code is invalid. Remaining attempts: $attemptsRemainingVerify",
                        Toast.LENGTH_SHORT
                    ).show()
                    isCountdownActiveVerify = true
                    attemptsRemainingVerify = 3
                } else if (isCountdownActiveVerify) {
                    binding.signupText.visibility = View.VISIBLE
                    binding.reSendHere.visibility = View.VISIBLE
                    Toast.makeText(
                        this@OTPEmailActivity,
                        "Try re-send the code",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@OTPEmailActivity,
                        "OTP code is invalid. Remaining attempts: $attemptsRemainingVerify",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(
                            this@OTPEmailActivity,
                            "The code is Valid!",
                            Toast.LENGTH_SHORT
                        ).show()
                        /*saveEmail()*/
                        userID = auth.currentUser!!.uid
                        val plan = "none"
                        val DocumentReference = firestore.collection("user").document(userID)
                        val userData = hashMapOf(
                            "firstname" to firstname,
                            "lastname" to lastname,
                            "email" to email,
                            "mobile" to mobile,
                            "password" to password,
                            "plan" to plan
                        )
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

        binding.reSendHere.setOnClickListener {
            attemptsRemainingResend--
            if (attemptsRemainingResend == 0 && !isCountdownActiveResend) {
                binding.signupText.visibility = View.INVISIBLE
                binding.reSendHere.visibility = View.INVISIBLE
                startReSend()
            } else if (isCountdownActiveResend) {
                Toast.makeText(
                    this@OTPEmailActivity,
                    "Try again after ${countdownTimeLeft / 1000} second",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                binding.signupText.visibility = View.INVISIBLE
                binding.reSendHere.visibility = View.INVISIBLE
                isCountdownActiveVerify = false
            }
        }


        random()
        setFocusable()
    }

    fun random(){
        random=Random.nextInt(100000..999999)
        var mail=SendMail("droidbytes11@gmail.com","ojjzedbyawubvwlv",email,"Signup app's OTP",
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
    }

    private fun startReSend() {
        isCountdownActiveResend = true
        countdownTimer = object : CountDownTimer(300000, 1000) { // 300000 ms = 5 menit
            override fun onTick(millisUntilFinished: Long) {
                countdownTimeLeft = millisUntilFinished // Simpan waktu yang tersisa
            }

            override fun onFinish() {
                isCountdownActiveResend = false
                Toast.makeText(
                    this@OTPEmailActivity,
                    "You can try to verify again",
                    Toast.LENGTH_SHORT
                ).show()
                attemptsRemainingResend = 3
                attemptsRemainingVerify = 3
            }
        }.start()
    }
}
