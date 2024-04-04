package com.example.capstoneproject.ui.home

import android.Manifest
import android.annotation.SuppressLint
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
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.capstoneproject.R
import com.example.capstoneproject.adapter.BannerAdapter
import com.example.capstoneproject.adapter.HomeArticlesAdapter
import com.example.capstoneproject.databinding.FragmentHomeBinding
import com.example.capstoneproject.model.Banner
import com.example.capstoneproject.ui.change_plan.ChangePlanActivity
import com.example.capstoneproject.ui.usage.UsageActivity
import com.example.capstoneproject.ui.wifi.WifiActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import java.util.Timer
import java.util.TimerTask

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var viewPager: ViewPager2
    private lateinit var timer: Timer
    private val MY_LOCATION_REQUEST_CODE = 123
//    private val db = Firebase.firestore
//    private val userID = FirebaseAuth.getInstance().currentUser!!.uid

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

//        getCurrentService(userID, viewLifecycleOwner)

        binding.scrollView.viewTreeObserver.addOnScrollChangedListener {
            binding.swipeRefresh.isEnabled = binding.scrollView.scrollY == 0
        }

//        binding.swipeRefresh.setOnRefreshListener {
//            getCurrentService(userID, viewLifecycleOwner)
//        }

        setupBanner()
        setupAction()
        setupArticles()
        checkAccessLocation()

        return root
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.cardPlanElevation.cardElevation = resources.getDimension(R.dimen.elevation_0dp)
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.cardPlanElevation.cardElevation = resources.getDimension(R.dimen.elevation_2dp)
            binding.progressBar.visibility = View.GONE
        }
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

//    private fun getCurrentService(userID: String, lifecycleOwner: LifecycleOwner) {
//        viewModel.setLoading(true)
//        db.collection("transaction")
//            .whereEqualTo("idUser", userID)
//            .get()
//            .addOnSuccessListener { querySnapshot ->
//                val lastTransaction =
//                    querySnapshot.sortedByDescending { it.get("timestamp").toString() }
//                        .firstOrNull()
//                if (lastTransaction != null) {
//                    val idOrder = lastTransaction.get("idOrder").toString()
//                    val service = viewModel.getService(lastTransaction.get("idService").toString())
//                    Log.d("DataUsageActivity", "Service Here Fragment =>> $service")
//
//                    viewModel.checkStatusTransaction(idOrder)
//                    viewModel.lastTrasaction.observe(lifecycleOwner) { status ->
//                        when (status) {
//                            "Success" -> {
//                                updatePlanAfterTransaction(userID, service, idOrder, status)
//                            }
//
//                            "Expired" -> {
//                                updateLastTransaction(idOrder, status)
//                            }
//                        }
//                    }
//                    getPlanFromDb(userID)
//                    binding.btnChangePlan.isEnabled = true
//                    binding.swipeRefresh.isRefreshing = false
//                    viewModel.setLoading(false)
//                } else {
//                    setUICurrentPlan("None")
//                    binding.btnChangePlan.isEnabled = false
//                    binding.swipeRefresh.isRefreshing = false
//                    viewModel.setLoading(false)
//                }
//            }
//    }

//    private fun updatePlanAfterTransaction(
//        idUser: String,
//        service: String,
//        idOrder: String,
//        status: String
//    ) {
//        db.collection("user")
//            .whereEqualTo("uid", idUser)
//            .limit(1)
//            .get()
//            .addOnSuccessListener { data ->
//                if (!data.isEmpty) {
//                    val userDocument = data.documents[0]
//                    db.collection("user").document(userDocument.id).update("plan", service)
//                    updateLastTransaction(idOrder, status)
//                } else {
//                    Log.d("HomeFragment", "Something went wrong")
//                }
//            }
//    }

//    private fun updateLastTransaction(idOrder: String, status: String) {
//        db.collection("transaction")
//            .whereEqualTo("idOrder", idOrder)
//            .limit(1)
//            .get()
//            .addOnSuccessListener { data ->
//                if (!data.isEmpty) {
//                    val transactionDocument = data.documents[0]
//                    db.collection("transaction").document(transactionDocument.id)
//                        .update("status", status)
//                } else {
//                    Log.d("HomeFragment", "Something went wrong")
//                }
//            }
//    }

//    private fun getPlanFromDb(idUser: String) {
//        db.collection("user")
//            .whereEqualTo("uid", idUser)
//            .limit(1)
//            .get()
//            .addOnSuccessListener { data ->
//                Log.d("HomeFragment", data.size().toString())
//                if (!data.isEmpty) {
//                    val userDocument = data.documents[0]
//                    val plan = userDocument.get("plan").toString()
//                    Log.e("PlanValue", "Plan Value: $plan")
//                    setUICurrentPlan(plan)
//                } else {
//                    Log.d("HomeFragment", "Something went wrong")
//                }
//            }
//    }

    @SuppressLint("ResourceType")
    private fun setUICurrentPlan(plan: String) {
        if (isAdded && context != null) {
            when (plan) {
                "Gold" -> {
                    binding.backgroundNoPlan.visibility = View.GONE

                    binding.cardPackage.setBackgroundResource(R.drawable.plan_gold)

                    binding.tvCurrentPackage.text = "Gold"
                    binding.tvCurrentPackage.setTextColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.gold
                        )
                    )

                    binding.tvCurrentSpeed.text = "Speed up to 50 mb/s"
                    binding.tvCurrentSpeed.setTextColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.gold
                        )
                    )

                    binding.tvCurrentServiceDate.setTextColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.gold
                        )
                    )

                    binding.tvCurrentLocation.setTextColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.gold
                        )
                    )

                    binding.btnChangePlan.backgroundTintList =
                        ContextCompat.getColorStateList(requireContext(), R.color.gold)

                    binding.tvChangePlan.text = "Change Plan"

                    binding.tvCurrentServiceDate.text = "Service date : 15  September 2023"

                    binding.tvCurrentLocation.text =
                        "Location : Dharmasushada Indah VI No. 100, Surabaya"

                    binding.btnChangePlanStyle.setBackgroundResource(R.drawable.cardview_change_plan_border)

                    binding.cardPlanElevation.cardElevation =
                        resources.getDimension(R.dimen.elevation_2dp)

                }

                "Silver" -> {
                    binding.backgroundNoPlan.visibility = View.GONE

                    binding.cardPackage.setBackgroundResource(R.drawable.plan_silver)

                    binding.tvCurrentPackage.text = "Silver"
                    binding.tvCurrentPackage.setTextColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.silver
                        )
                    )

                    binding.tvCurrentSpeed.text = "Speed up to 30 mb/s"
                    binding.tvCurrentSpeed.setTextColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.silver
                        )
                    )

                    binding.tvCurrentServiceDate.setTextColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.silver
                        )
                    )

                    binding.tvCurrentLocation.setTextColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.silver
                        )
                    )

                    binding.btnChangePlan.backgroundTintList =
                        ContextCompat.getColorStateList(requireContext(), R.color.silver)

                    binding.tvChangePlan.text = "Change Plan"

                    binding.tvCurrentServiceDate.text = "Service date : 15  September 2023"

                    binding.tvCurrentLocation.text =
                        "Location : Dharmasushada Indah VI No. 100, Surabaya"

                    binding.btnChangePlanStyle.setBackgroundResource(R.drawable.cardview_change_plan_border)

                    binding.cardPlanElevation.cardElevation =
                        resources.getDimension(R.dimen.elevation_2dp)

                }

                "Bronze" -> {
                    binding.backgroundNoPlan.visibility = View.GONE

                    binding.cardPackage.setBackgroundResource(R.drawable.plan_bronze)

                    binding.tvCurrentPackage.text = "Bronze"
                    binding.tvCurrentPackage.setTextColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.bronze
                        )
                    )

                    binding.tvCurrentSpeed.text = "Speed up to 15 mb/s"
                    binding.tvCurrentSpeed.setTextColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.bronze
                        )
                    )

                    binding.tvCurrentServiceDate.setTextColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.bronze
                        )
                    )

                    binding.tvCurrentLocation.setTextColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.bronze
                        )
                    )

                    binding.btnChangePlan.backgroundTintList =
                        ContextCompat.getColorStateList(requireContext(), R.color.bronze)

                    binding.tvChangePlan.text = "Change Plan"

                    binding.tvCurrentServiceDate.text = "Service date : 15  September 2023"

                    binding.tvCurrentLocation.text =
                        "Location : Dharmasushada Indah VI No. 100, Surabaya"

                    binding.btnChangePlanStyle.setBackgroundResource(R.drawable.cardview_change_plan_border)

                    binding.cardPlanElevation.cardElevation =
                        resources.getDimension(R.dimen.elevation_2dp)

                }

                else -> {
                    binding.backgroundNoPlan.visibility = View.VISIBLE

                }
            }
        }
    }

    private val bannerList = listOf(
        Banner("Satu", "https://wow.net.id/wp-content/uploads/2023/05/3-1536x768.jpg"),
        Banner("Dua", "https://wow.net.id/wp-content/uploads/2023/05/2-1536x768.jpg"),
        Banner("Tiga", "https://wow.net.id/wp-content/uploads/2023/05/1-1536x768.jpg")
    )

    private fun setupBanner() {
        viewPager = binding.banner
        val adapter = BannerAdapter(bannerList)
        viewPager.adapter = adapter

        // Supaya gambar ga load dulu
        val requestManager = Glide.with(this)
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                // Preload gambar untuk memuat ke dalam cache
                val preloadItems = bannerList.subList(positionStart, positionStart + itemCount)
                preloadItems.forEach { banner ->
                    requestManager.load(banner.url)
                        .diskCacheStrategy(DiskCacheStrategy.DATA) // atau jenis lainnya
                        .preload()
                }
            }
        })
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
        val slideDelay = 4000L
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
            startActivity(Intent(requireContext(), ChangePlanActivity::class.java))
        }
    }

    private fun setupArticles() {
        viewModel.listArticle.observe(viewLifecycleOwner) { listArticles ->
            binding.rvArticles.setHasFixedSize(true)
            binding.rvArticles.layoutManager = LinearLayoutManager(requireContext())
            val adapter = HomeArticlesAdapter(listArticles)
            binding.rvArticles.adapter = adapter
        }
    }
}