package com.example.capstoneproject.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.capstoneproject.MainActivity
import com.example.capstoneproject.R
import com.example.capstoneproject.databinding.FragmentHomeBinding
import com.example.capstoneproject.ui.login.LoginActivity
import com.example.capstoneproject.ui.change_plan.ChangePlanActivity
import com.example.capstoneproject.ui.usage.UsageActivity
import com.example.capstoneproject.ui.wifi.WifiActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupToolbar()

        setupAction()

        return root
    }

    // for setup toolbar
    private fun setupToolbar() {
        val toolbar = binding.toolbar

        toolbar.setNavigationOnClickListener {
            MainActivity.isLogin = false
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}