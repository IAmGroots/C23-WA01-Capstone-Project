package com.example.capstoneproject.ui.articles.detail_article

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.capstoneproject.adapter.ArticlesMoreAdapter
import com.example.capstoneproject.databinding.ActivityDetailArticlesBinding
import com.example.capstoneproject.model.Articles

class DetailArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailArticlesBinding
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailArticlesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupArticle()

    }

    private fun setupToolbar() {
        val toolbar = binding.toolbar

        toolbar.setNavigationOnClickListener {
            super.onBackPressed()
        }
    }

    private fun setupArticle() {
        val data = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_ARTICLE, Articles::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_ARTICLE)
        }

        if (data != null) {
            binding.titleArticle.text = data.title
            binding.publishDate.text = data.publishDate
            binding.descArticle.text = data.content
            Glide.with(this)
                .load(data?.image)
                .into(binding.imgArticles)

            viewModel.getMoreArticles(data.id)
        }

        viewModel.listArticle.observe(this) { listArticles ->
            binding.rvArticlesMore.setHasFixedSize(true)
            binding.rvArticlesMore.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            val adapter = ArticlesMoreAdapter(listArticles)
            binding.rvArticlesMore.adapter = adapter
        }
    }

    companion object {
        const val EXTRA_ARTICLE = "extra_article"
    }
}