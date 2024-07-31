package com.example.capstoneproject.ui.usage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.content.ContextCompat
import com.example.capstoneproject.R
import com.example.capstoneproject.databinding.ActivityUsageBinding
import com.example.capstoneproject.ui.customView.customMarkerChart
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter

class UsageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUsageBinding
    private lateinit var barChart: BarChart
    private lateinit var yearSpinner: Spinner
    private lateinit var years: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        barChart = binding.chart
        yearSpinner = binding.yearSpinner

        fetchYearsFromApi()
        setupChart()
        setupToolbar()
        setupAction()
        setUICurrentPlan("")
    }

    private fun setupAction() {
        binding.btnChangePlan.setOnClickListener {
//            startActivity(Intent(requireContext(), ChangePlanActivity::class.java))
        }

        binding.btnShop.setOnClickListener {
//            startActivity(Intent(requireContext(), ChangePlanActivity::class.java))
        }
    }

    private fun setupChart() {
        updateChartForYear(years[0])
        barChart.legend.isEnabled = false
        barChart.xAxis.labelRotationAngle = -90f

        barChart.xAxis.setDrawGridLines(false)
        barChart.axisLeft.setDrawGridLines(false)
        barChart.xAxis.textSize = 12f
        barChart.axisLeft.textSize = 12f

        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        barChart.xAxis.granularity = 1f
        barChart.xAxis.labelCount = 12
        barChart.xAxis.valueFormatter = MonthAxisValueFormatter()
        barChart.axisRight.isEnabled = false
        barChart.setTouchEnabled(true)
        barChart.setPinchZoom(true)
        barChart.description.text = ""
        barChart.setNoDataText("No forex yet!")
        barChart.animateX(2000, Easing.EaseInExpo)
        barChart.xAxis.textColor = ContextCompat.getColor(this, R.color.secondaryColor)
        barChart.axisLeft.textColor = ContextCompat.getColor(this, R.color.secondaryColor)
        val markerView = customMarkerChart(this, R.layout.marker_view)
        barChart.marker = markerView
    }

    private fun fetchYearsFromApi() {
        // Simulasi panggilan API
        years = listOf("2021", "2022", "2023") // Gantikan ini dengan panggilan API asli
        updateSpinnerWithYears(years)
    }

    private fun updateSpinnerWithYears(years: List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, years)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        yearSpinner.adapter = adapter

        // Set a listener to handle item selection
        yearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedYear = parent.getItemAtPosition(position) as String
                updateChartForYear(selectedYear)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle case where no item is selected if needed
            }
        }
    }

    private fun updateChartForYear(year: String) {
        // Fetch data for the selected year (this could be another API call)
        val usage = getDataForYear(year)
        val entries = ArrayList<BarEntry>()

        for ((index, data) in usage.withIndex()) {
            Log.d("INDEX", "$index: x | y :$data")
            entries.add(BarEntry(index.toFloat(), data.toFloat()))
        }

        val vl = BarDataSet(entries, "Data for $year")
        vl.setDrawValues(false)
        vl.color = ContextCompat.getColor(this, R.color.contentColor)

        barChart.data = BarData(vl)
        barChart.invalidate() // Refresh the chart
    }

    private fun getDataForYear(year: String): List<Int> {
        // Simulate fetching data based on the year
        // Replace this with actual data retrieval logic
        return when (year) {
            "2021" -> listOf(15, 11, 13, 22, 13, 20, 26, 22, 18, 32, 30, 38)
            "2022" -> listOf(12, 9, 14, 18, 16, 22, 27, 19, 20, 29, 24, 35)
            "2023" -> listOf(10, 8, 16, 20, 14, 23, 25, 21, 17, 30, 25, 40)
            else -> listOf()
        }
    }

    private fun setupToolbar() {
        val toolbar = binding.toolbar

        toolbar.setNavigationOnClickListener {
            super.onBackPressed()
        }
    }

    private fun setUICurrentPlan(plan: String) {
        when (plan) {
            "Gold" -> {
                binding.cardPlanElevation.visibility = View.VISIBLE
                binding.cardPackageNone.visibility = View.GONE
                binding.cardPackage.setBackgroundResource(R.drawable.plan_gold)
                binding.tvCurrentPackage.text = "Gold"
                binding.tvCurrentPackage.setTextColor(ContextCompat.getColor(this, R.color.gold))
                binding.tvCurrentSpeed.text = "Speed up to 50 mb/s"
                binding.tvCurrentSpeed.setTextColor(ContextCompat.getColor(this, R.color.gold))
                binding.tvCurrentServiceDate.setTextColor(ContextCompat.getColor(this, R.color.gold))
                binding.tvCurrentLocation.setTextColor(ContextCompat.getColor(this, R.color.gold))
                binding.btnChangePlan.backgroundTintList = ContextCompat.getColorStateList(this, R.color.gold)
                binding.tvCurrentServiceDate.text = "Service date : 15  September 2023"
                binding.tvCurrentLocation.text = "Location : Dharmasushada Indah VI No. 100, Surabaya"
                binding.cardPlanElevation.cardElevation = resources.getDimension(R.dimen.elevation_2dp)
            }
            "Silver" -> {
                binding.cardPlanElevation.visibility = View.VISIBLE
                binding.cardPackageNone.visibility = View.GONE
                binding.cardPackage.setBackgroundResource(R.drawable.plan_silver)
                binding.tvCurrentPackage.text = "Silver"
                binding.tvCurrentPackage.setTextColor(ContextCompat.getColor(this, R.color.silver))
                binding.tvCurrentSpeed.text = "Speed up to 30 mb/s"
                binding.tvCurrentSpeed.setTextColor(ContextCompat.getColor(this, R.color.silver))
                binding.tvCurrentServiceDate.setTextColor(ContextCompat.getColor(this, R.color.silver))
                binding.tvCurrentLocation.setTextColor(ContextCompat.getColor(this, R.color.silver))
                binding.btnChangePlan.backgroundTintList = ContextCompat.getColorStateList(this, R.color.silver)
                binding.tvCurrentServiceDate.text = "Service date : 15  September 2023"
                binding.tvCurrentLocation.text = "Location : Dharmasushada Indah VI No. 100, Surabaya"
                binding.cardPlanElevation.cardElevation = resources.getDimension(R.dimen.elevation_2dp)
            }
            "Bronze" -> {
                binding.cardPlanElevation.visibility = View.VISIBLE
                binding.cardPackageNone.visibility = View.GONE
                binding.cardPackage.setBackgroundResource(R.drawable.plan_bronze)
                binding.tvCurrentPackage.text = "Bronze"
                binding.tvCurrentPackage.setTextColor(ContextCompat.getColor(this, R.color.bronze))
                binding.tvCurrentSpeed.text = "Speed up to 15 mb/s"
                binding.tvCurrentSpeed.setTextColor(ContextCompat.getColor(this, R.color.bronze))
                binding.tvCurrentServiceDate.setTextColor(ContextCompat.getColor(this, R.color.bronze))
                binding.tvCurrentLocation.setTextColor(ContextCompat.getColor(this, R.color.bronze))
                binding.btnChangePlan.backgroundTintList = ContextCompat.getColorStateList(this, R.color.bronze)
                binding.tvCurrentServiceDate.text = "Service date : 15  September 2023"
                binding.tvCurrentLocation.text = "Location : Dharmasushada Indah VI No. 100, Surabaya"
                binding.cardPlanElevation.cardElevation = resources.getDimension(R.dimen.elevation_2dp)
            }
            else -> {
                binding.cardPackageNone.visibility = View.VISIBLE
                binding.cardPlanElevation.visibility = View.GONE
            }
        }
    }
}

class MonthAxisValueFormatter : ValueFormatter() {
    private val months = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return months.getOrNull(value.toInt()) ?: value.toString()
    }
}