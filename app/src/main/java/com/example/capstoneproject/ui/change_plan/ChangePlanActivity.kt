package com.example.capstoneproject.ui.change_plan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.capstoneproject.MainActivity
import com.example.capstoneproject.databinding.ActivityChangePlanBinding
import com.example.capstoneproject.ui.payment.PaymentActivity

class ChangePlanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePlanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()

        binding.btnUpgrade.setOnClickListener {
            startActivity(Intent(this, PaymentActivity::class.java))
        }
    }

    private fun setupToolbar() {
        val toolbar = binding.toolbar

        toolbar.setNavigationOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}