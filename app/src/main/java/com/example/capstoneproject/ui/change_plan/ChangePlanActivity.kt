package com.example.capstoneproject.ui.change_plan

import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.capstoneproject.R
import com.example.capstoneproject.databinding.ActivityChangePlanBinding
import com.example.capstoneproject.ui.order.OrderActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class ChangePlanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePlanBinding
    private var plan: String = ""
    private val db = Firebase.firestore
    private val userID = FirebaseAuth.getInstance().currentUser!!.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectCurrentPlan(userID)
        setupToolbar()

        binding.btnChangePlanGold.setOnClickListener {
            plan = "Gold"
            val detail = Intent(this, OrderActivity::class.java)
            detail.putExtra(OrderActivity.EXTRA_PACKAGE, plan)
            startActivity(detail)
        }

        binding.btnChangePlanSilver.setOnClickListener {
            plan = "Silver"
            val detail = Intent(this, OrderActivity::class.java)
            detail.putExtra(OrderActivity.EXTRA_PACKAGE, plan)
            startActivity(detail)
        }

        binding.btnChangePlanBronze.setOnClickListener {
            plan = "Bronze"
            val detail = Intent(this, OrderActivity::class.java)
            detail.putExtra(OrderActivity.EXTRA_PACKAGE, plan)
            startActivity(detail)
        }
    }

    private fun setupToolbar() {
        val toolbar = binding.toolbar

        toolbar.setNavigationOnClickListener {
            super.onBackPressed()
        }
    }

    private fun selectCurrentPlan(idUser: String) {
        db.collection("user")
            .whereEqualTo("uid", idUser)
            .limit(1)
            .get()
            .addOnSuccessListener { data ->
                if (!data.isEmpty) {
                    val userDocument = data.documents[0]
                    val plan = userDocument.get("plan").toString()
                    Log.d("ChangePlan", "Current Plan => $plan")
                    setUICurrentPlan(plan)
                } else {
                    Log.d("ChangePlan", "Something went wrong")
                }
            }
    }

    private fun setUICurrentPlan(plan: String) {
        when (plan) {
            "Gold" -> {
                binding.btnChangePlanGold.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.green))
                binding.btnChangePlanGold.isEnabled = false
                binding.tvPlanGold.text = "Currently"
                binding.tvPlanSilver.text = "Downgrade"
                binding.tvPlanBronze.text = "Downgrade"
            }
            "Silver" -> {
                binding.btnChangePlanSilver.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.green))
                binding.btnChangePlanSilver.isEnabled = false
                binding.tvPlanGold.text = "Upgrade"
                binding.tvPlanSilver.text = "Currently"
                binding.tvPlanBronze.text = "Downgrade"
            }
            "Bronze" -> {
                binding.btnChangePlanBronze.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.green))
                binding.btnChangePlanBronze.isEnabled = false
                binding.tvPlanGold.text = "Upgrade"
                binding.tvPlanSilver.text = "Upgrade"
                binding.tvPlanBronze.text = "Currently"
            }
            else -> {
                binding.tvPlanGold.text = "Buy"
                binding.tvPlanSilver.text = "Buy"
                binding.tvPlanBronze.text = "Buy"
            }
        }
    }
}