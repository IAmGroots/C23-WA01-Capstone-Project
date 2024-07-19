package com.example.capstoneproject.ui.articles

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstoneproject.adapter.ArticlesAdapter
import com.example.capstoneproject.databinding.FragmentArticlesBinding
import com.example.capstoneproject.preferences.ViewModelFactory
import com.example.capstoneproject.data.result.Result

class ArticlesFragment : Fragment() {

    private var _binding: FragmentArticlesBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ArticlesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticlesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val viewModelFactory: ViewModelFactory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[ArticlesViewModel::class.java]

        viewModel.isLoading.observe(requireActivity()) {
            showLoading(it)
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.setLoading(true)
            setupArticles()
            viewModel.userProfile.observe(requireActivity()) { profile ->
                Log.d("Articles", profile.token)
                val tokens = "Bearer ${profile.token}"
                viewModel.fetchArticles(tokens)
                binding.swipeRefresh.isRefreshing = false
            }
        }

        viewModel.setLoading(true)
        setupArticles()

        viewModel.userProfile.observe(requireActivity()) { profile ->
            Log.d("Articles", profile.token)
            val tokens = "Bearer ${profile.token}"
            viewModel.fetchArticles(tokens)
        }

        return root
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.rvArticles.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.INVISIBLE
            binding.rvArticles.visibility = View.VISIBLE
        }
    }

    private fun setupArticles() {
        viewModel.listArticles.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    Log.d("articles", "Loading")
                }
                is Result.Success -> {
                    Log.d("articles", "Success Get Data : ${result.data}")
                    binding.rvArticles.setHasFixedSize(true)
                    binding.rvArticles.layoutManager = LinearLayoutManager(requireContext())
                    val adapter = ArticlesAdapter(result.data.data)
                    binding.rvArticles.adapter = adapter
                    viewModel.setLoading(false)
                }
                is Result.Error -> {
//                    viewModel.setLoading(false)
                    Log.d("articles", "Error : ${result.error}")
                }
            }
        }
    }
}