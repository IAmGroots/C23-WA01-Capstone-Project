package com.example.capstoneproject.ui.profile

import android.content.Intent
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import androidx.lifecycle.LifecycleOwner
import com.example.capstoneproject.ui.history.HistoryActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ProfileViewModel
    private val DURATION: Long = 1000
    private val db = Firebase.firestore
    private val userID = FirebaseAuth.getInstance().currentUser!!.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val preferences = SettingPreferences.getInstance(requireActivity().application.dataStore)
        viewModel = ViewModelProvider(this, ViewModelFactory(preferences))[ProfileViewModel::class.java]

        loadUserData()
        setTheme()
        setBiometric()
        setActionButton()
        setupListHistoryPayment()
        setupSocialMediaLinks()

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        getCurrentService(userID, viewLifecycleOwner)

        binding.scrollViewProfile.viewTreeObserver.addOnScrollChangedListener {
            binding.swipeRefresh.isEnabled = binding.scrollViewProfile.scrollY == 0
        }

        binding.swipeRefresh.setOnRefreshListener {
            getCurrentService(userID, viewLifecycleOwner)
            Handler(Looper.getMainLooper()).postDelayed({
                binding.swipeRefresh.isRefreshing = false
            }, DURATION)
        }

        binding.containerLogout.setOnClickListener {
            viewModel.logout()
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        return root
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun loadUserData(){
        val sharedPreferences = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val firstName = sharedPreferences.getString("firstName", "")
        val lastName = sharedPreferences.getString("lastName", "")
        val email = sharedPreferences.getString("email", "")

        // Menampilkan informasi pengguna di TextView pada profil
        binding.tvFullName.text = "$firstName $lastName"
        binding.tvEmail.text = email
    }

    private fun setActionButton() {
        binding.btnEditProfile.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }

        binding.btnChangePlan.setOnClickListener {
            startActivity(Intent(requireContext(), ChangePlanActivity::class.java))
        }

        binding.btnShop.setOnClickListener {
            startActivity(Intent(requireContext(), ChangePlanActivity::class.java))
        }

        binding.containerHistoryTransaction.setOnClickListener {
            startActivity(Intent(requireActivity(), HistoryActivity::class.java))
        }
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
            if (isChecked) {
                Toast.makeText(requireContext(), "Biometric Fingerprint Enabled", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Biometric Fingerprint Disabled", Toast.LENGTH_SHORT).show()
            }
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

    private fun getCurrentService(userID: String, lifecycleOwner: LifecycleOwner) {
        viewModel.setLoading(true)
        db.collection("transaction").whereEqualTo("idUser", userID).get()
            .addOnSuccessListener { querySnapshot ->
                val lastTransaction =
                    querySnapshot.sortedByDescending { it.get("timestamp").toString() }
                        .firstOrNull()
                lastTransaction?.let {
                    // Lakukan operasi yang diperlukan di sini
                    val idOrder = it.get("idOrder").toString()
                    val service = viewModel.getService(it.get("idService").toString())
                    Log.d("DataUsageActivity", "Service Here Fragment =>> $service")

                    viewModel.checkStatusTransaction(idOrder)
                    viewModel.lastTrasaction.observe(lifecycleOwner) { status ->
                        when (status) {
                            "Success" -> {
                                updatePlanAfterTransaction(idOrder, status)
                            }

                            "Expired" -> {
                                updateLastTransaction(idOrder, status)
                            }
                        }
                    }
                    getPlanFromDb(userID)
                }
            }
        viewModel.setLoading(false)
    }

    private fun updatePlanAfterTransaction(idUser: String, service: String) {
        db.collection("user").whereEqualTo("uid", idUser).limit(1).get()
            .addOnSuccessListener { data ->
                if (!data.isEmpty) {
                    val userDocument = data.documents[0]
                    db.collection("user").document(userDocument.id).update("plan", service)
                } else {
                    Log.d("ProfileFragment", "Something went wrong")
                }
            }
    }

    private fun updateLastTransaction(idOrder: String, status: String) {
        db.collection("transaction").whereEqualTo("idOrder", idOrder).limit(1).get()
            .addOnSuccessListener { data ->
                if (!data.isEmpty) {
                    val transactionDocument = data.documents[0]
                    db.collection("user").document(transactionDocument.id).update("status", status)
                } else {
                    Log.d("ProfileFragment", "Something went wrong")
                }
            }
    }

    private fun getPlanFromDb(idUser: String) {
        db.collection("user").whereEqualTo("uid", idUser).limit(1).get()
            .addOnSuccessListener { data ->
                Log.d("ProfileFragment", data.size().toString())
                if (!data.isEmpty) {
                    val userDocument = data.documents[0]
                    val plan = userDocument.get("plan").toString()
                    setUICurrentPlan(plan)
                } else {
                    Log.d("ProfileFragment", "Something went wrong")
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
            }

            "Silver" -> {
                binding.cardPackageNone.visibility = View.GONE

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
            }

            "Bronze" -> {
                binding.cardPackageNone.visibility = View.GONE

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