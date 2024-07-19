package com.example.capstoneproject.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstoneproject.R
import com.example.capstoneproject.adapter.BannerSliderAdapter
import com.example.capstoneproject.adapter.BannerSliderItem
import com.example.capstoneproject.adapter.HomeArticlesAdapter
import com.example.capstoneproject.data.result.Result
import com.example.capstoneproject.databinding.FragmentHomeBinding
import com.example.capstoneproject.preferences.ViewModelFactory
import com.example.capstoneproject.ui.usage.UsageActivity
import com.example.capstoneproject.ui.wifi.WifiActivity
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import koleton.api.hideSkeleton
import koleton.api.loadSkeleton

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private val MY_LOCATION_REQUEST_CODE = 123

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val viewModelFactory: ViewModelFactory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[HomeViewModel::class.java]

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        binding.scrollView.viewTreeObserver.addOnScrollChangedListener {
            binding.swipeRefresh.isEnabled = binding.scrollView.scrollY == 0
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.setLoading(true)
            setupBanner()
            setupArticles()
            checkAccessLocation()
            setUICurrentPlan("Bronze")
            viewModel.userProfile.observe(requireActivity()) { profile ->
                Log.d("Articles", profile.token)
                val tokens = "Bearer ${profile.token}"
                viewModel.fetchArticles(tokens)
                binding.swipeRefresh.isRefreshing = false
            }
        }

        viewModel.setLoading(true)
        setupBanner()
        setupAction()
        setupArticles()
        checkAccessLocation()
        setUICurrentPlan("Bronze")

        viewModel.userProfile.observe(requireActivity()) { profile ->
            Log.d("Articles", profile.token)
            val tokens = "Bearer ${profile.token}"
            viewModel.fetchArticles(tokens)
        }
//        viewModel.setLoading(false)
        return root
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            showSkeleton()
        } else {
            hideSkeleton()
        }
    }

    private fun showSkeleton() {
        binding.skeleton.visibility = View.VISIBLE
        binding.content.visibility = View.INVISIBLE
        binding.layoutBannerSkeleton.loadSkeleton {
            color(R.color.skeleton)
        }
        binding.layoutMenuSkeleton.loadSkeleton {
            color(R.color.skeleton)
        }
        binding.layoutCardPlanSkeleton.loadSkeleton {
            color(R.color.skeleton)
        }
        binding.rvArticlesSkeleton.setHasFixedSize(true)
        binding.rvArticlesSkeleton.layoutManager = LinearLayoutManager(requireContext())
        val adapter = HomeArticlesAdapter(emptyList())
        binding.rvArticlesSkeleton.adapter = adapter
        binding.rvArticlesSkeleton.loadSkeleton(R.layout.home_article_items) {
            itemCount(4)
            color(R.color.skeleton)
        }
    }

    private fun hideSkeleton() {
        binding.content.visibility = View.VISIBLE
        binding.skeleton.visibility = View.INVISIBLE
        binding.layoutBannerSkeleton.hideSkeleton()
        binding.layoutMenuSkeleton.hideSkeleton()
        binding.layoutCardPlanSkeleton.hideSkeleton()
        binding.rvArticlesSkeleton.hideSkeleton()
    }

    private fun checkAccessLocation() {
        if (!isAccessLocationGranted()) {
            requestLocationPermission()
        }
    }

    private fun isAccessLocationGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            MY_LOCATION_REQUEST_CODE
        )
    }

    private fun setUICurrentPlan(plan: String) {
        when (plan) {
            "Gold" -> {
                binding.cardPlanElevation.visibility = View.VISIBLE
                binding.cardPackageNone.visibility = View.GONE
                binding.cardPackage.setBackgroundResource(R.drawable.plan_gold)
                binding.tvCurrentPackage.text = "Gold"
                binding.tvCurrentPackage.setTextColor(ContextCompat.getColor(requireContext(), R.color.gold))
                binding.tvCurrentSpeed.text = "Speed up to 50 mb/s"
                binding.tvCurrentSpeed.setTextColor(ContextCompat.getColor(requireContext(), R.color.gold))
                binding.tvCurrentServiceDate.setTextColor(ContextCompat.getColor(requireContext(), R.color.gold))
                binding.tvCurrentLocation.setTextColor(ContextCompat.getColor(requireContext(), R.color.gold))
                binding.btnChangePlan.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.gold)
                binding.tvCurrentServiceDate.text = "Service date : 15  September 2023"
                binding.tvCurrentLocation.text = "Location : Dharmasushada Indah VI No. 100, Surabaya"
                binding.cardPlanElevation.cardElevation = resources.getDimension(R.dimen.elevation_2dp)
            }
            "Silver" -> {
                binding.cardPlanElevation.visibility = View.VISIBLE
                binding.cardPackageNone.visibility = View.GONE
                binding.cardPackage.setBackgroundResource(R.drawable.plan_silver)
                binding.tvCurrentPackage.text = "Silver"
                binding.tvCurrentPackage.setTextColor(ContextCompat.getColor(requireContext(), R.color.silver))
                binding.tvCurrentSpeed.text = "Speed up to 30 mb/s"
                binding.tvCurrentSpeed.setTextColor(ContextCompat.getColor(requireContext(), R.color.silver))
                binding.tvCurrentServiceDate.setTextColor(ContextCompat.getColor(requireContext(), R.color.silver))
                binding.tvCurrentLocation.setTextColor(ContextCompat.getColor(requireContext(), R.color.silver))
                binding.btnChangePlan.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.silver)
                binding.tvCurrentServiceDate.text = "Service date : 15  September 2023"
                binding.tvCurrentLocation.text = "Location : Dharmasushada Indah VI No. 100, Surabaya"
                binding.cardPlanElevation.cardElevation = resources.getDimension(R.dimen.elevation_2dp)
            }
            "Bronze" -> {
                binding.cardPlanElevation.visibility = View.VISIBLE
                binding.cardPackageNone.visibility = View.GONE
                binding.cardPackage.setBackgroundResource(R.drawable.plan_bronze)
                binding.tvCurrentPackage.text = "Bronze"
                binding.tvCurrentPackage.setTextColor(ContextCompat.getColor(requireContext(), R.color.bronze))
                binding.tvCurrentSpeed.text = "Speed up to 15 mb/s"
                binding.tvCurrentSpeed.setTextColor(ContextCompat.getColor(requireContext(), R.color.bronze))
                binding.tvCurrentServiceDate.setTextColor(ContextCompat.getColor(requireContext(), R.color.bronze))
                binding.tvCurrentLocation.setTextColor(ContextCompat.getColor(requireContext(), R.color.bronze))
                binding.btnChangePlan.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.bronze)
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

    private fun setupBanner() {
        var sliderList = arrayListOf(
            BannerSliderItem(R.drawable.banner_1),
            BannerSliderItem(R.drawable.banner_2),
            BannerSliderItem(R.drawable.banner_3)
        )

        var bannerSliderAdapter = BannerSliderAdapter(sliderList)
        val bannerSliderView: SliderView = binding.imageSlider
        bannerSliderView.setSliderAdapter(bannerSliderAdapter)
        bannerSliderView.setIndicatorAnimation(IndicatorAnimationType.WORM)
        bannerSliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        bannerSliderView.scrollTimeInSec = 3
        bannerSliderView.startAutoCycle()
    }

    private fun setupAction() {
        binding.btnWifi.setOnClickListener {
            startActivity(Intent(requireContext(), WifiActivity::class.java))
        }

        binding.btnUsage.setOnClickListener {
//            Log.d("HomeFragmet","Masuk ke Usage")
            startActivity(Intent(requireContext(), UsageActivity::class.java))
        }

        binding.btnChangePlan.setOnClickListener {
//            startActivity(Intent(requireContext(), ChangePlanActivity::class.java))
        }

        binding.btnShop.setOnClickListener {
//            startActivity(Intent(requireContext(), ChangePlanActivity::class.java))
        }
    }

    private fun setupArticles() {
        viewModel.listArticles.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    Log.d("articles", "Loading")
                }
                is Result.Success -> {
                    Log.d("articles", "Success Get Data : ${result.data}")
                    binding.rvArticles.setHasFixedSize(true)
                    binding.rvArticles.layoutManager = LinearLayoutManager(requireContext())
                    val adapter = HomeArticlesAdapter(result.data.data?.take(4))
                    binding.rvArticles.adapter = adapter
                    viewModel.setLoading(false)
                }
                is Result.Error -> {
                    viewModel.setLoading(false)
                    Log.d("articles", "Error : ${result.error}")
                }
            }
        }
    }
}