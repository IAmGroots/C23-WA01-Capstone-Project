package com.example.capstoneproject.ui.articles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstoneproject.adapter.ArticlesAdapter
import com.example.capstoneproject.databinding.FragmentArticlesBinding

class ArticlesFragment : Fragment() {

    private var _binding: FragmentArticlesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ArticlesViewModel by viewModels()

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