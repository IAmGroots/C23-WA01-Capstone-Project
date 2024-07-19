package com.example.capstoneproject.ui.articles.detail_article

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.capstoneproject.R
import com.example.capstoneproject.adapter.ArticlesMoreAdapter
import com.example.capstoneproject.data.result.Result
import com.example.capstoneproject.databinding.ActivityDetailArticlesBinding
import com.example.capstoneproject.preferences.ViewModelFactory

class DetailArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailArticlesBinding
    private lateinit var viewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailArticlesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModelFactory: ViewModelFactory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[DetailViewModel::class.java]

        setupToolbar()
//        setupArticle()

        viewModel.userProfile.observe(this) { profile ->
            Log.d("Articles", profile.token)
            val tokens = "Bearer ${profile.token}"
            val articleId = intent.getStringExtra(EXTRA_ARTICLE)
            Log.d("Articles", "Id : ${articleId}")
            if (articleId != null) {
                viewModel.fetchArticles(articleId, tokens)
                viewModel.fetchListArticles(tokens)
            }
        }

        viewModel.articles.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    Log.d("articles", "Loading")
                }
                is Result.Success -> {
                    val article = result.data
                    Log.d("articles", "Success Get Data : ${article}")
                    binding.publishDate.text = "01 Januari 2023"
                    binding.titleArticle.text = article.data?.title
                    binding.descArticle.text = article.data?.content
                    Glide.with(this)
                        .load(R.drawable.example_image_article)
                        .into(binding.imgArticles)
                }
                is Result.Error -> {
                    Log.d("articles", "Error : ${result.error}")
                }
            }
        }

        viewModel.listArticles.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    Log.d("articles", "Loading")
                }
                is Result.Success -> {
                    Log.d("articles", "Success Get Data : ${result.data}")
                    binding.rvArticlesMore.setHasFixedSize(true)
                    binding.rvArticlesMore.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                    val adapter = ArticlesMoreAdapter(result.data.data)
                    binding.rvArticlesMore.adapter = adapter
                }
                is Result.Error -> {
                    Log.d("articles", "Error : ${result.error}")
                }
            }
        }

    }

    private fun setupToolbar() {
        val toolbar = binding.toolbar

        toolbar.setNavigationOnClickListener {
            super.onBackPressed()
        }
    }

    companion object {
        const val EXTRA_ARTICLE = "extra_article"
    }
}