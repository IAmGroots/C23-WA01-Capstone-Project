package com.example.capstoneproject.ui.otp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.capstoneproject.MainActivity
import com.example.capstoneproject.databinding.ActivityOtpmobileBinding
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

class OTPMobileActivity : AppCompatActivity() {
    /*companion object {
        const val MY_PERMISSIONS_REQUEST_SEND_SMS = 1
    }*/

    private lateinit var binding: ActivityOtpmobileBinding
    /*private val viewModel: OTPViewModel by viewModels()*/
    private val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    private val userID = FirebaseAuth.getInstance().currentUser!!.uid
    private var attemptsRemainingVerify = 3 // Jumlah percobaan verifikasi tersisa
    private var attemptsRemainingResend = 3 // Jumlah percobaan resend otp tersisa
    private var isCountdownActiveResend = false // Apakah countdown aktif
    private lateinit var countdownTimer: CountDownTimer
    private var countdownTimeLeft: Long = 0
    private var verification: String? = null
    private var firstname = ""
    private var lastname = ""
    private var mobile = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpmobileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        /*if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.SEND_SMS),
                MY_PERMISSIONS_REQUEST_SEND_SMS
            )
        }*/

        firstname = intent.getStringExtra("firstname").toString()
        lastname = intent.getStringExtra("lastname").toString()
        mobile = intent.getStringExtra("mobile").toString()
        sendOTP(mobile)

        binding.signupText.visibility = View.INVISIBLE
        binding.reSendhere.visibility = View.INVISIBLE

        /*firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")*/
        /*FirebaseApp.initializeApp(this)*/

        setFocusable()

        /*val otpCode = generateRandomOTP()
        sendOtpSms(mobile, otpCode)*/
        val otpCode = sendOTP(mobile)
        Log.e("iniloh si otp code","$otpCode")

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
                        verify(otp)
                    }
                }
            }
        }

        binding.reSendhere.setOnClickListener {
            if (attemptsRemainingResend > 0) {
                sendOTP(mobile)
                /*val otpCode = generateRandomOTP()
                sendOtpSms(mobile, otpCode)*/
                binding.reSendhere.isEnabled = true
                binding.signupText.visibility = View.INVISIBLE
                binding.reSendhere.visibility = View.INVISIBLE
                attemptsRemainingResend--
                enableVerifyOTP()
            } else {
                startResendCountdown()
            }
        }
    }

    private fun disableVerifyOTP() {
        binding.btnverifydotp.isEnabled = false
        Toast.makeText(this@OTPMobileActivity, "Try re-send the code", Toast.LENGTH_SHORT).show()
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
                        this@OTPMobileActivity,
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
        binding.reSendhere.isEnabled = true
        binding.signupText.visibility = View.VISIBLE
        binding.reSendhere.visibility = View.VISIBLE
        isCountdownActiveResend = false
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

    private val callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.d("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "ERROR:", e)
                Toast.makeText(this@OTPMobileActivity, "Verification Failed", Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(input: String, token: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(input, token)
                verification = input
                Toast.makeText(this@OTPMobileActivity, "Send OTP code...", Toast.LENGTH_SHORT).show()
            }
        }

    private fun verify(codeOTP: String) {
        val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(verification!!, codeOTP)
        update(credential)
    }

    private fun update(credential: PhoneAuthCredential) {
        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(OnCompleteListener<AuthResult?> { task ->
                if (task.isSuccessful) {
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
                    if (attemptsRemainingVerify > 1) {
                        Toast.makeText(
                            this,
                            "Incorrect OTP! Attempts remaining: ${attemptsRemainingVerify - 1}",
                            Toast.LENGTH_SHORT
                        ).show()
                        attemptsRemainingVerify--
                    } else {
                        disableVerifyOTP()
                        enableResend()
                    }
                }
            })
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
