package com.example.capstoneproject.ui.profile

import android.content.Intent
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.example.capstoneproject.MainActivity
import com.example.capstoneproject.R
import com.example.capstoneproject.adapter.HistoryPaymentAdapter
import com.example.capstoneproject.databinding.FragmentProfileBinding
import com.example.capstoneproject.preferences.SettingPreferences
import com.example.capstoneproject.preferences.ViewModelFactory
import com.example.capstoneproject.preferences.dataStore
import com.example.capstoneproject.ui.change_plan.ChangePlanActivity
import com.example.capstoneproject.ui.login.LoginActivity
import com.example.capstoneproject.ui.profile.edit_profile.EditProfileActivity
import androidx.biometric.BiometricManager

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val preferences = SettingPreferences.getInstance(requireActivity().application.dataStore)
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(preferences)
        )[ProfileViewModel::class.java]

        setTheme()
        setBiometric()

        binding.btnEditProfile.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }

        binding.btnChangePlan.setOnClickListener {
            startActivity(Intent(requireContext(), ChangePlanActivity::class.java))
        }

        binding.btnShop.setOnClickListener {
            startActivity(Intent(requireContext(), ChangePlanActivity::class.java))
        }

        binding.containerLogout.setOnClickListener {
            MainActivity.isLogin = false
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        loadUserDataFromSharedPreferences()
        setupListHistoryPayment()
        setupSocialMediaLinks()


        return root
    }

    private fun setTheme() {
        viewModel.getTheme().observe(viewLifecycleOwner) { isDarkModeActive ->
            binding.switchMode.isChecked = isDarkModeActive
        }

        binding.switchMode.setOnCheckedChangeListener { _, isChecked ->
            setAppTheme(isChecked)
            viewModel.saveTheme(isChecked)
        }
    }

    private fun setBiometric() {
        val biometricManager = BiometricManager.from(requireContext())
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                binding.containerBiometricFingerprint.visibility = View.GONE
            }
        }

        viewModel.getBiometric().observe(viewLifecycleOwner) { isEnableBiometric ->
            binding.switchBiometric.isChecked = isEnableBiometric
        }
        binding.switchBiometric.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveBiometric(isChecked)
        }
    }

    private fun setAppTheme(isDarkMode: Boolean) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }


    private fun loadUserDataFromSharedPreferences() {
        val sharedPreferences =
            requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val firstName = sharedPreferences.getString("firstName", "")
        val lastName = sharedPreferences.getString("lastName", "")
        val email = sharedPreferences.getString("email", "")
//      val phone = sharedPreferences.getString("phone", "")
        val plan = sharedPreferences.getString("plan", "")

        // Menampilkan informasi pengguna di TextView pada profil
        binding.tvFullName.text = "$firstName $lastName"
        binding.tvEmail.text = email

        when (plan) {
            "gold" -> {
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

            "silver" -> {
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

            "bronze" -> {
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
        val intent = Intent(Intent.ACTION_VIEW)
        val url = getSocialMediaURL(socialMedia)
        intent.data = Uri.parse(url)

        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
        } else {
            // Jika aplikasi tidak terpasang, arahkan ke Google menggunakan browser
            val googleURL = "https://www.google.com/search?q=$socialMedia"
            val googleIntent = Intent(Intent.ACTION_VIEW)
            googleIntent.data = Uri.parse(googleURL)

            if (googleIntent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(googleIntent)
            } else {
                // Jika tidak ada aplikasi browser yang terpasang, tampilkan pesan kesalahan
                Toast.makeText(requireContext(), "Tidak dapat membuka browser", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    private fun getSocialMediaURL(socialMedia: String): String {
        // Fungsi untuk mengembalikan URL dari media sosial sesuai dengan yang dipilih
        return when (socialMedia) {
            "twitter" -> "https://twitter.com/wownetid/"
            "instagram" -> "https://www.instagram.com/wownet.id/"
            "facebook" -> "https://www.facebook.com/wownet.id/"
            "linkedin" -> "https://www.linkedin.com/company/wownet-wowrack-cepat-nusantara/"
            else -> ""
        }
    }

    private fun setupListHistoryPayment() {
        binding.rvHistoryPayment.setHasFixedSize(true)
        binding.rvHistoryPayment.layoutManager = LinearLayoutManager(requireContext())
        viewModel.listHistoryPayment.observe(viewLifecycleOwner) { listHistoryPayment ->
            val adapter = HistoryPaymentAdapter(listHistoryPayment)
            Log.d("PROFILE", listHistoryPayment.toString())
            binding.rvHistoryPayment.adapter = adapter
        }
    }
}