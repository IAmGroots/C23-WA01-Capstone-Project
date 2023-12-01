package com.example.capstoneproject.ui.profile

import android.content.Intent
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstoneproject.MainActivity
import com.example.capstoneproject.R
import com.example.capstoneproject.adapter.HistoryPaymentAdapter
import com.example.capstoneproject.databinding.DialogSocialMediaBinding
import com.example.capstoneproject.databinding.FragmentProfileBinding
import com.example.capstoneproject.preferences.SettingPreferences
import com.example.capstoneproject.preferences.ViewModelFactory
import com.example.capstoneproject.preferences.dataStore
import com.example.capstoneproject.ui.biometric.BiometricActivity
import com.example.capstoneproject.ui.change_plan.ChangePlanActivity
import com.example.capstoneproject.ui.login.LoginViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ProfileViewModel
    private val loginViewModel: LoginViewModel by viewModels()

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

        binding.cardSocialMedia.setOnClickListener {
            showSocialMedia()
        }

        setBiometric()
        setAction()
        loadUserDataFromSharedPreferences()
        setupListHistoryPayment()

        return root
    }

    private fun showSocialMedia() {
//        val dialog = RoundedBottomSheetDialog(requireContext())
        val dialog = BottomSheetDialog(requireContext())
        val binding = DialogSocialMediaBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        binding.btnFacebook.setOnClickListener {
            openSocialMedia("Facebook")
        }
        binding.btnInstagram.setOnClickListener {
            openSocialMedia("Instagram")
        }
        binding.btnTwitter.setOnClickListener {
            openSocialMedia("Twitter")
        }
        binding.btnLinkedIn.setOnClickListener {
            openSocialMedia("LinkedIn")
        }
        dialog.show()
    }

    private fun openSocialMedia(app: String) {
        when (app) {
            "Facebook" -> {
                val facebookUrl = "https://www.facebook.com/"
                val facebookIntent = Intent(Intent.ACTION_VIEW)
                facebookIntent.data = Uri.parse(facebookUrl)

                if (isAppInstalled("com.facebook.katana")) {
                    facebookIntent.setPackage("com.facebook.katana")
                }

                startActivity(facebookIntent)
            }
            "Instagram" -> {
                val instagramUrl = "https://www.instagram.com/"
                val instagramIntent = Intent(Intent.ACTION_VIEW)
                instagramIntent.data = Uri.parse(instagramUrl)

                if (isAppInstalled("com.instagram.android")) {
                    instagramIntent.setPackage("com.instagram.android")
                }

                startActivity(instagramIntent)
            }
            "Twitter" -> {
                val twitterUrl = "https://twitter.com/"
                val twitterIntent = Intent(Intent.ACTION_VIEW)
                twitterIntent.data = Uri.parse(twitterUrl)

                if (isAppInstalled("com.twitter.android")) {
                    twitterIntent.setPackage("com.twitter.android")
                }

                startActivity(twitterIntent)
            }
            "LinkedIn" -> {
                val linkedinUrl = "https://www.linkedin.com/"
                val linkedinIntent = Intent(Intent.ACTION_VIEW)
                linkedinIntent.data = Uri.parse(linkedinUrl)

                if (isAppInstalled("com.linkedin.android")) {
                    linkedinIntent.setPackage("com.linkedin.android")
                }

                startActivity(linkedinIntent)
            }
            else -> {
                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isAppInstalled(packageName: String): Boolean {
        return try {
            requireContext().packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun setBiometric() {
        viewModel.getHasBiometric().observe(viewLifecycleOwner) { hasBiometric ->
            binding.cardBiometricFingerprint.visibility = if (hasBiometric) View.VISIBLE else View.GONE
        }

        viewModel.getBiometric().observe(viewLifecycleOwner) { isEnableBiometric ->
            binding.switchBiometric.isChecked = isEnableBiometric
        }
    }

    private fun setAction() {
        binding.switchBiometric.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveBiometric(isChecked)
        }

        binding.btnChangePlan.setOnClickListener {
            startActivity(Intent(requireContext(), ChangePlanActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            MainActivity.isLogin = false
            viewModel.saveBiometric(false)
            val intent = Intent(requireContext(), BiometricActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    private fun loadUserDataFromSharedPreferences() {
        val sharedPreferences = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val firstName = sharedPreferences.getString("firstName", "")
        val lastName = sharedPreferences.getString("lastName", "")
        val email = sharedPreferences.getString("email", "")
        val phone = sharedPreferences.getString("phone", "")

        // Menampilkan informasi pengguna di TextView pada profil
        binding.tvFullName.text = firstName + " " + lastName
        binding.tvEmail.text = email
        binding.tvPhone.text = phone
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}