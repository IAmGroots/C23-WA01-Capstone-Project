package com.example.capstoneproject.ui.profile

import android.content.Intent
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstoneproject.MainActivity
import com.example.capstoneproject.adapter.HistoryPaymentAdapter
import com.example.capstoneproject.databinding.FragmentProfileBinding
import com.example.capstoneproject.preferences.SettingPreferences
import com.example.capstoneproject.preferences.ViewModelFactory
import com.example.capstoneproject.preferences.dataStore
import com.example.capstoneproject.ui.biometric.BiometricActivity
import com.example.capstoneproject.ui.change_plan.ChangePlanActivity
import com.example.capstoneproject.ui.login.LoginViewModel

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
// <<<<<<< Hadi
    private lateinit var viewModel: ProfileViewModel
// =======
    private val viewModel: ProfileViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()
// >>>>>>> main

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

// <<<<<<< Hadi
        val preferences = SettingPreferences.getInstance(requireActivity().application.dataStore)
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(preferences)
        )[ProfileViewModel::class.java]

        viewModel.getBiometric().observe(viewLifecycleOwner) { isEnableBiometric ->
            binding.switchBiometric.isChecked = isEnableBiometric
        }

        binding.switchBiometric.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveBiometric(isChecked)
        }

        viewModel.getFullname().observe(viewLifecycleOwner) { fullname ->
            binding.tvFullName.text = fullname
        }

        viewModel.getEmail().observe(viewLifecycleOwner) { email ->
            binding.tvEmail.text = email
        }

        viewModel.getPhone().observe(viewLifecycleOwner) { phone ->
            binding.tvPhone.text = phone
        }

        binding.btnChangePlan.setOnClickListener {
            startActivity(Intent(requireContext(), ChangePlanActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            MainActivity.isLogin = false
            val intent = Intent(requireContext(), BiometricActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

=======
        loadUserDataFromSharedPreferences()
// >>>>>>> main
        setupListHistoryPayment()

        return root
    }

    private fun loadUserDataFromSharedPreferences() {
        val sharedPreferences = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val firstName = sharedPreferences.getString("firstName", "")
        val lastName = sharedPreferences.getString("lastName", "")
        val email = sharedPreferences.getString("email", "")

        // Menampilkan informasi pengguna di TextView pada profil
        binding.tvFirstName.text = firstName
        binding.tvLastName.text = lastName
        binding.tvEmail.text = email
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