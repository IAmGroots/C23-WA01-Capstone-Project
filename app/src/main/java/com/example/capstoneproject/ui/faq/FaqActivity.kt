package com.example.capstoneproject.ui.faq

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstoneproject.adapter.FaqAdapter
import com.example.capstoneproject.databinding.ActivityFaqBinding
import com.example.capstoneproject.model.DataSourceFaq

class FaqActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFaqBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaqBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerview()
    }

    private fun setupToolbar() {
        val toolbar = binding.toolbar

        toolbar.setNavigationOnClickListener {
            super.onBackPressed()
        }
    }

    private fun setupRecyclerview() {
        val listFaq = DataSourceFaq.dataFaq
        binding.rvFaq.setHasFixedSize(true)
        binding.rvFaq.layoutManager = LinearLayoutManager(this)
        val adapter = FaqAdapter(listFaq)
        binding.rvFaq.adapter = adapter
    }
}