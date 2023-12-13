package com.example.capstoneproject.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.example.capstoneproject.adapter.FirebaseMessageAdapter
import com.example.capstoneproject.adapter.HomeArticlesAdapter
import com.example.capstoneproject.databinding.FragmentHomeBinding
import com.example.capstoneproject.model.Banner
import com.example.capstoneproject.ui.change_plan.ChangePlanActivity
import com.example.capstoneproject.ui.chat.ChatActivity
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
    private val DURATION: Long = 1000
    private val db = Firebase.firestore
    private val userID = FirebaseAuth.getInstance().currentUser!!.uid

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

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        getCurrentService(userID, viewLifecycleOwner)

        binding.scrollView.viewTreeObserver.addOnScrollChangedListener {
            binding.swipeRefresh.isEnabled = binding.scrollView.scrollY == 0
        }

        binding.swipeRefresh.setOnRefreshListener {
            getCurrentService(userID, viewLifecycleOwner)
        }

        binding.btnChat.setOnClickListener {
            startActivity(Intent(requireContext(), ChatActivity::class.java))
        }

        return root
    }

    private fun showLoading(isLoading: Boolean) {
        binding.cardPackage.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.cardPackageNone.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun getCurrentService(userID: String, lifecycleOwner: LifecycleOwner) {
        viewModel.setLoading(true)
        db.collection("transaction")
            .whereEqualTo("idUser", userID)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val lastTransaction =
                    querySnapshot.sortedByDescending { it.get("timestamp").toString() }
                        .firstOrNull()
                if (lastTransaction != null) {
                    val idOrder = lastTransaction.get("idOrder").toString()
                    val service = viewModel.getService(lastTransaction.get("idService").toString())
                    Log.d("DataUsageActivity", "Service Here Fragment =>> $service")

                    viewModel.checkStatusTransaction(idOrder)
                    viewModel.lastTrasaction.observe(lifecycleOwner) { status ->
                        when (status) {
                            "Success" -> {
                                updatePlanAfterTransaction(userID, service, idOrder, status)
                            }

                            "Expired" -> {
                                updateLastTransaction(idOrder, status)
                            }
                        }
                    }
                    getPlanFromDb(userID)
                    binding.btnChangePlan.isEnabled = true
                    binding.swipeRefresh.isRefreshing = false
                    viewModel.setLoading(false)
                } else {
                    setUICurrentPlan("None")
                    binding.btnChangePlan.isEnabled = false
                    binding.swipeRefresh.isRefreshing = false
                    viewModel.setLoading(false)
                }
            }
    }

    private fun updatePlanAfterTransaction(
        idUser: String,
        service: String,
        idOrder: String,
        status: String
    ) {
        db.collection("user")
            .whereEqualTo("uid", idUser)
            .limit(1)
            .get()
            .addOnSuccessListener { data ->
                if (!data.isEmpty) {
                    val userDocument = data.documents[0]
                    db.collection("user").document(userDocument.id).update("plan", service)
                    updateLastTransaction(idOrder, status)
                } else {
                    Log.d("HomeFragment", "Something went wrong")
                }
            }
    }

    private fun updateLastTransaction(idOrder: String, status: String) {
        db.collection("transaction")
            .whereEqualTo("idOrder", idOrder)
            .limit(1)
            .get()
            .addOnSuccessListener { data ->
                if (!data.isEmpty) {
                    val transactionDocument = data.documents[0]
                    db.collection("transaction").document(transactionDocument.id)
                        .update("status", status)
                } else {
                    Log.d("HomeFragment", "Something went wrong")
                }
            }
    }

    private fun getPlanFromDb(idUser: String) {
        db.collection("user")
            .whereEqualTo("uid", idUser)
            .limit(1)
            .get()
            .addOnSuccessListener { data ->
                Log.d("HomeFragment", data.size().toString())
                if (!data.isEmpty) {
                    val userDocument = data.documents[0]
                    val plan = userDocument.get("plan").toString()
                    setUICurrentPlan(plan)
                } else {
                    Log.d("HomeFragment", "Something went wrong")
                }
            }
    }

    private fun setUICurrentPlan(plan: String) {
        when (plan) {
            "Gold" -> {
                binding.cardPackageNone.visibility = View.GONE

                binding.cardPackage.setBackgroundResource(R.drawable.plan_gold)

                binding.tvCurrentPackage.text = "Gold"
                binding.tvCurrentPackage.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.gold
                    )
                )

                binding.tvCurrentSpeed.text = "Speed up to 50 mb/s"
                binding.tvCurrentSpeed.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.gold
                    )
                )

                binding.tvCurrentServiceDate.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.gold
                    )
                )

                binding.tvCurrentLocation.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.gold
                    )
                )

                binding.btnChangePlan.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.gold)
            }

            "Silver" -> {
                binding.cardPackageNone.visibility = View.GONE

                binding.cardPackage.setBackgroundResource(R.drawable.plan_silver)

                binding.tvCurrentPackage.text = "Silver"
                binding.tvCurrentPackage.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.silver
                    )
                )

                binding.tvCurrentSpeed.text = "Speed up to 30 mb/s"
                binding.tvCurrentSpeed.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.silver
                    )
                )

                binding.tvCurrentServiceDate.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.silver
                    )
                )

                binding.tvCurrentLocation.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.silver
                    )
                )

                binding.btnChangePlan.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.silver)
            }

            "Bronze" -> {
                binding.cardPackageNone.visibility = View.GONE

                binding.cardPackage.setBackgroundResource(R.drawable.plan_bronze)

                binding.tvCurrentPackage.text = "Bronze"
                binding.tvCurrentPackage.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.bronze
                    )
                )

                binding.tvCurrentSpeed.text = "Speed up to 15 mb/s"
                binding.tvCurrentSpeed.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.bronze
                    )
                )

                binding.tvCurrentServiceDate.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.bronze
                    )
                )

                binding.tvCurrentLocation.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.bronze
                    )
                )

                binding.btnChangePlan.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.bronze)

            }

            else -> {
                binding.cardPackage.visibility = View.GONE
            }
        }
    }

    // for setup toolbar
    private fun setupToolbar() {
        val toolbar = binding.toolbar

        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_notifications -> {
                    Toast.makeText(
                        requireContext(),
                        "Icon Notification Click By $userID",
                        Toast.LENGTH_SHORT
                    ).show()
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