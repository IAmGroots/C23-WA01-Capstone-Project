package com.example.capstoneproject.ui.articles

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstoneproject.R
import com.example.capstoneproject.adapter.ArticlesAdapter
import com.example.capstoneproject.databinding.FragmentArticlesBinding
import com.example.capstoneproject.model.Articles
import com.example.capstoneproject.model.DataSourceArticles
import com.google.android.material.bottomnavigation.BottomNavigationView

class ArticlesFragment : Fragment() {

    private var _binding: FragmentArticlesBinding? = null
    private val binding get() = _binding!!
    private val viewModel : ArticlesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticlesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupListArticles()

        return root
    }

    private fun setupListArticles() {
        binding.rvArticles.setHasFixedSize(true)
        binding.rvArticles.layoutManager = LinearLayoutManager(requireContext())
        viewModel.listArticle.observe(viewLifecycleOwner) { listArticles ->
            val adapter = ArticlesAdapter(listArticles)
            binding.rvArticles.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}