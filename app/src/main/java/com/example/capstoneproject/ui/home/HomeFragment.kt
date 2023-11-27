package com.example.capstoneproject.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.capstoneproject.MainActivity
import com.example.capstoneproject.R
import com.example.capstoneproject.adapter.ArticlesMoreAdapter
import com.example.capstoneproject.adapter.BannerAdapter
import com.example.capstoneproject.adapter.HomeArticlesAdapter
import com.example.capstoneproject.databinding.FragmentHomeBinding
import com.example.capstoneproject.model.Banner
import com.example.capstoneproject.ui.articles.ArticlesViewModel
import com.example.capstoneproject.ui.biometric.BiometricActivity
import com.example.capstoneproject.ui.login.LoginActivity
import com.example.capstoneproject.ui.change_plan.ChangePlanActivity
import com.example.capstoneproject.ui.usage.UsageActivity
import com.example.capstoneproject.ui.wifi.WifiActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Timer
import java.util.TimerTask

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel : HomeViewModel by viewModels()
    private lateinit var viewPager: ViewPager2
    private lateinit var timer: Timer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root



        setupToolbar()
        setupBanner()
        setupAction()
        setupArticles()

//        val navController = requireActivity().findNavController(R.id.nav_host_fragment)
//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            when (destination.id) {
//                R.id.navigation_news -> {
//                    Toast.makeText(requireContext(), "News", Toast.LENGTH_SHORT).show()
//                }
//                R.id.navigation_settings -> {
//                    Toast.makeText(requireContext(), "Settings", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
        return root
    }

    // for setup toolbar
    private fun setupToolbar() {
        val toolbar = binding.toolbar

        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_notifications -> {
                    Toast.makeText(requireContext(), "Icon Notification Clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> true
            }
        }
    }

    // Banner Start
    private val bannerList = listOf(
        Banner("Satu", "https://wow.net.id/wp-content/uploads/2023/05/3-1536x768.jpg"),
        Banner("Dua", "https://wow.net.id/wp-content/uploads/2023/05/2-1536x768.jpg"),
        Banner("Tiga", "https://wow.net.id/wp-content/uploads/2023/05/1-1536x768.jpg")
    )

    private fun setupBanner() {
        viewPager = binding.banner
        val adapter = BannerAdapter(bannerList)
        viewPager.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        startSlideShow()
    }

    override fun onPause() {
        super.onPause()
        stopSlideShow()
    }

    private fun startSlideShow() {
        val slideDelay = 5000L
        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                requireActivity().runOnUiThread {
                    val currentItem = viewPager.currentItem
                    if (currentItem == bannerList.size - 1) {
                        viewPager.currentItem = 0
                    } else {
                        viewPager.currentItem = currentItem + 1
                    }
                }
            }
        }, slideDelay)
    }

    private fun stopSlideShow() {
        timer.cancel()
    }
    // Banner Finish

    private fun setupAction() {
        binding.btnWifi.setOnClickListener {
            startActivity(Intent(requireContext(), WifiActivity::class.java))
        }

        binding.btnUsage.setOnClickListener {
            startActivity(Intent(requireContext(), UsageActivity::class.java))
        }

        binding.btnChangePlan.setOnClickListener {
            startActivity(Intent(requireContext(), ChangePlanActivity::class.java))
        }

        binding.btnShop.setOnClickListener {
            Toast.makeText(requireContext(), "Shop was Clicked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupArticles() {
        viewModel.listArticle.observe(viewLifecycleOwner) { listArticles ->
            binding.rvArticles.setHasFixedSize(true)
            binding.rvArticles.layoutManager = LinearLayoutManager(requireContext())
            val adapter = HomeArticlesAdapter(listArticles)
            binding.rvArticles.adapter = adapter
        }

//        val marginTopBottom = 16
//        val marginSeparator = 4
//        val density = binding.root.context.resources.displayMetrics.density
//        val topBottom = (marginTopBottom * density).toInt()
//        val separator = (marginSeparator * density).toInt()
//        val layoutParams = binding.cardArticles.layoutParams as CardView.LayoutParams
//        layoutParams.bottomMargin = topBottom
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}