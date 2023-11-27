package com.example.capstoneproject.ui.profile

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstoneproject.adapter.HistoryPaymentAdapter
import com.example.capstoneproject.databinding.FragmentProfileBinding
import com.example.capstoneproject.ui.login.LoginViewModel

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        loadUserDataFromSharedPreferences()
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
            binding.rvHistoryPayment.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}