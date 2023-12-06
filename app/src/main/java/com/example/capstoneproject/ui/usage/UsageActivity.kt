package com.example.capstoneproject.ui.usage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.capstoneproject.MainActivity
import com.example.capstoneproject.R
import com.example.capstoneproject.databinding.ActivityUsageBinding
import com.example.capstoneproject.ui.customView.customMarkerChart
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class UsageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUsageBinding
    private val db = Firebase.firestore
    private val userID = FirebaseAuth.getInstance().currentUser!!.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupChart()
        setupToolbar()

//        getCurrentService(userID)
        selectCurrentPlan(userID)
    }

    private fun setupChart() {
        //Part1
        val entries = ArrayList<Entry>()
        val lineChart = binding.chart

//        val usage = listOf(10, 5, 8, 20, 14, 23, 25, 21, 17)
        val usage = listOf(10, 8, 16, 20, 14, 23, 25, 21, 17, 30, 25, 40)
//        val usage = listOf(10, 5, 8, 20, 14, 23, 25, 21, 17, 30, 8, 40, 12, 15, 20)

        for ((index, data) in usage.withIndex()) {
            val x = index
            val y = data
            Log.d("INDEX", x.toString() + ": x | y :" + y.toString())
            entries.add(Entry(x.toFloat(), y.toFloat()))
        }

        val vl = LineDataSet(entries, "Data Usage")

        vl.setDrawValues(false)
        vl.setDrawFilled(true)
        vl.lineWidth = 3f
//        vl.fillColor = R.color.red
//        vl.fillAlpha = R.color.red
        vl.setDrawCircles(true)
        vl.circleHoleRadius = 10f

        lineChart.xAxis.labelRotationAngle = -45f

        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChart.xAxis.granularity = 1f
        lineChart.xAxis.valueFormatter = MonthAxisValueFormatter()
        lineChart.data = LineData(vl)
        lineChart.axisRight.isEnabled = false
        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)
        lineChart.description.text = ""
        lineChart.setNoDataText("No forex yet!")
        lineChart.animateX(2000, Easing.EaseInExpo)
        val markerView = customMarkerChart(this, R.layout.marker_view)
        lineChart.marker = markerView
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
                    Log.d("ChangePLan", "Current Plan => $plan")
                    setUICurrentPlan(plan)
                } else {
                    Log.d("DataUsage", "Something went wrong")
                }
            }
    }

    private fun setUICurrentPlan(plan: String) {
        when (plan) {
            "Gold" -> {
                binding.cardPackageNone.visibility = View.GONE

                binding.cardPackage.setBackgroundResource(R.drawable.plan_gold)

                binding.tvCurrentPackage.text = "Gold"
                binding.tvCurrentPackage.setTextColor(ContextCompat.getColor(this, R.color.gold))

                binding.tvCurrentSpeed.text = "Speed up to 50 mb/s"
                binding.tvCurrentSpeed.setTextColor(ContextCompat.getColor(this, R.color.gold))

                binding.tvCurrentServiceDate.setTextColor(ContextCompat.getColor(this, R.color.gold))

                binding.tvCurrentLocation.setTextColor(ContextCompat.getColor(this, R.color.gold))

                binding.btnChangePlan.backgroundTintList = ContextCompat.getColorStateList(this, R.color.gold)
            }
            "Silver" -> {
                binding.cardPackageNone.visibility = View.GONE

                binding.cardPackage.setBackgroundResource(R.drawable.plan_silver)

                binding.tvCurrentPackage.text = "Silver"
                binding.tvCurrentPackage.setTextColor(ContextCompat.getColor(this, R.color.silver))

                binding.tvCurrentSpeed.text = "Speed up to 30 mb/s"
                binding.tvCurrentSpeed.setTextColor(ContextCompat.getColor(this, R.color.silver))

                binding.tvCurrentServiceDate.setTextColor(ContextCompat.getColor(this, R.color.silver))

                binding.tvCurrentLocation.setTextColor(ContextCompat.getColor(this, R.color.silver))

                binding.btnChangePlan.backgroundTintList = ContextCompat.getColorStateList(this, R.color.silver)
            }
            "Bronze" -> {
                binding.cardPackageNone.visibility = View.GONE

                binding.cardPackage.setBackgroundResource(R.drawable.plan_bronze)

                binding.tvCurrentPackage.text = "Bronze"
                binding.tvCurrentPackage.setTextColor(ContextCompat.getColor(this, R.color.bronze))

                binding.tvCurrentSpeed.text = "Speed up to 15 mb/s"
                binding.tvCurrentSpeed.setTextColor(ContextCompat.getColor(this, R.color.bronze))

                binding.tvCurrentServiceDate.setTextColor(ContextCompat.getColor(this, R.color.bronze))

                binding.tvCurrentLocation.setTextColor(ContextCompat.getColor(this, R.color.bronze))

                binding.btnChangePlan.backgroundTintList = ContextCompat.getColorStateList(this, R.color.bronze)

            }
            else -> {
                binding.cardPackage.visibility = View.GONE
            }
        }
    }
}

class MonthAxisValueFormatter : ValueFormatter() {
    private val months = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        val index = value.toInt() % months.size
        return months[index]
    }
}