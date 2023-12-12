package com.example.capstoneproject.ui.customView

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.capstoneproject.R
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF

@SuppressLint("ViewConstructor")
class customMarkerChart(context: Context, layoutResource: Int):  MarkerView(context, layoutResource) {
    private val tvUsage: TextView = findViewById(R.id.tv_usage)

    init {
        // Mendapatkan warna yang diinginkan dari resource color
        val textColor = ContextCompat.getColor(context, R.color.primaryColor)
        tvUsage.setTextColor(textColor)
    }

    override fun refreshContent(entry: Entry?, highlight: Highlight?) {
        val value = entry?.y?.toDouble() ?: 0.0
        var resText = ""
        if(value.toString().length > 8){
            resText = value.toString().substring(0,7) + " GB"
        }
        else{
            resText = "$value GB"
        }
        tvUsage.text = resText
        super.refreshContent(entry, highlight)
    }

    override fun getOffsetForDrawingAtPoint(xpos: Float, ypos: Float): MPPointF {
        return MPPointF(-width / 2f, -height - 10f)
    }
}