package com.example.capstoneproject.ui.profile

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.example.capstoneproject.R
import com.example.capstoneproject.databinding.FragmentProfileBinding
import com.example.capstoneproject.preferences.ViewModelFactory
import com.example.capstoneproject.ui.login.LoginActivity
import com.example.capstoneproject.ui.profile.edit_profile.EditProfileActivity
import androidx.biometric.BiometricManager
import com.example.capstoneproject.ui.faq.FaqActivity
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val viewModelFactory: ViewModelFactory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[ProfileViewModel::class.java]

        viewModel.setLoading(true)
        checkHasBiometric()
        setupToolbar()
        loadUserData()
        setBiometric()
        setActionButton()
        setupSocialMediaLinks()
//        getCurrentService(userID, viewLifecycleOwner)

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        binding.scrollViewProfile.viewTreeObserver.addOnScrollChangedListener {
            binding.swipeRefresh.isEnabled = binding.scrollViewProfile.scrollY == 0
        }
//
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.setLoading(true)
            loadUserData()
            binding.swipeRefresh.isRefreshing = false
        }

        viewModel.getTheme().observe(viewLifecycleOwner) { isDarkModeActive ->
            AppCompatDelegate.setDefaultNightMode(if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
            binding.switchDarkMode.isChecked = isDarkModeActive
        }

        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveTheme(isChecked)
        }

        viewModel.getBiometric().observe(viewLifecycleOwner) { isBiometricActive ->
            binding.switchBiometric.isChecked = isBiometricActive
        }

        binding.switchBiometric.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveBiometric(isChecked)
        }

        return root
    }

    private fun setupToolbar() {
        val toolbar = binding.toolbar

        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_logout -> {
                    viewModel.logout()
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    true
                }

                else -> true
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.cardPlanElevation.cardElevation = resources.getDimension(R.dimen.elevation_0dp)
            binding.containerProfile.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.cardPlanElevation.cardElevation = resources.getDimension(R.dimen.elevation_2dp)
            binding.progressBar.visibility = View.INVISIBLE
            binding.containerProfile.visibility = View.VISIBLE
        }
    }

    private fun checkHasBiometric() {
        val biometricManager = BiometricManager.from(requireContext())
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                binding.containerBiometricFingerprint.visibility = View.VISIBLE
            }
            else -> {
                binding.containerBiometricFingerprint.visibility = View.GONE
            }
        }
    }

    private fun loadUserData() {
        viewModel.userProfile.observe(viewLifecycleOwner) { profile ->
            val fullname = "${profile.firstName} ${profile.lastName}"
            binding.tvEmail.text = profile.email
            binding.tvFullName.text = fullname
        }
        setUICurrentPlan("")
        viewModel.setLoading(false)
    }

    private fun setActionButton() {
        binding.btnEditProfile.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }

        binding.btnChangePlan.setOnClickListener {
//            startActivity(Intent(requireContext(), ChangePlanActivity::class.java))
        }

        binding.btnShop.setOnClickListener {
//            startActivity(Intent(requireContext(), ChangePlanActivity::class.java))
        }

        binding.containerHistoryTransaction.setOnClickListener {
            Toast.makeText(requireContext(), "Fitur dalam pengembangan", Toast.LENGTH_SHORT).show()
//            startActivity(Intent(requireActivity(), HistoryActivity::class.java))
        }

        binding.containerFaq.setOnClickListener {
            startActivity(Intent(requireContext(), FaqActivity::class.java))
        }

        binding.containerCustomerService.setOnClickListener {
            Toast.makeText(requireContext(), "Fitur dalam pengembangan", Toast.LENGTH_SHORT).show()
//            startActivity(Intent(requireContext(), ChatActivity::class.java))
        }
    }

    private fun setBiometric() {
        val biometricManager = BiometricManager.from(requireContext())
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                binding.containerBiometricFingerprint.visibility = View.GONE
            }
        }
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

    private fun setupSocialMediaLinks() {
        val gridLayout = binding.gridLayoutSocialMedia

        for (i in 0 until gridLayout.childCount) {
            val view = gridLayout.getChildAt(i)
            if (view is LottieAnimationView) {
                // Set tag untuk setiap LottieAnimationView, misalnya menggunakan nama sosial media
                view.setTag(getSocialMediaName(i)) // getSocialMediaName harus mengembalikan string yang unik
                view.setOnClickListener {
                    openSocialMedia(
                        view.getTag()?.toString() ?: ""
                    ) // Memanggil fungsi untuk membuka media sosial
                }
            }
        }
    }

    // Fungsi untuk mendapatkan nama sosial media berdasarkan posisi dalam GridLayout
    private fun getSocialMediaName(position: Int): String {
        return when (position) {
            0 -> "twitter"
            1 -> "instagram"
            2 -> "facebook"
            3 -> "linkedin"
            else -> ""
        }
    }

    private fun openSocialMedia(socialMedia: String) {
        val url = getSocialMediaURL(socialMedia)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }


    private fun getSocialMediaURL(socialMedia: String): String {
        // Fungsi untuk mengembalikan URL dari media sosial sesuai dengan yang dipilih
        return when (socialMedia) {
            "twitter" -> "https://x.com/wownetid?t=FQU58MvlmpVZ5Gr04ti37Q&s=09"
            "instagram" -> "https://www.instagram.com/wownet.id/"
            "facebook" -> "https://www.facebook.com/wownet.id/"
            "linkedin" -> "https://www.linkedin.com/company/wownet-wowrack-cepat-nusantara/"
            else -> ""
        }
    }

}