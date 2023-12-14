package com.example.capstoneproject.ui.history

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.capstoneproject.R
import com.example.capstoneproject.adapter.SectionsPagerAdapter
import com.example.capstoneproject.databinding.ActivityHistoryBinding
import com.google.android.material.tabs.TabLayoutMediator
class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupTabLayout()
    }

    private fun setupToolbar() {
        val toolbar = binding.toolbar

        toolbar.setNavigationOnClickListener {
            super.onBackPressed()
        }
    }

    private fun setupTabLayout() {
        val tabs = binding.tabs
        val viewPager = binding.viewPager
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
    }

    companion object {
        private val TAB_TITLES = intArrayOf(
            R.string.tab1,
            R.string.tab2,
            R.string.tab3
        )
    }
}