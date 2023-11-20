package com.example.capstoneproject.ui.payment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.capstoneproject.MainActivity
import com.example.capstoneproject.databinding.ActivityPaymentBinding
import com.example.capstoneproject.ui.change_plan.ChangePlanActivity
import com.example.capstoneproject.ui.order.OrderActivity

class PaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()

        binding.btnPay.setOnClickListener {
            startActivity(Intent(this, OrderActivity::class.java))
        }
    }

    private fun setupToolbar() {
        val toolbar = binding.toolbar

        toolbar.setNavigationOnClickListener {
            startActivity(Intent(this, ChangePlanActivity::class.java))
        }
    }
}