package com.example.capstoneproject.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.capstoneproject.databinding.ArticlesMoreItemsBinding
import com.example.capstoneproject.model.Articles
import com.example.capstoneproject.ui.articles.detail_article.DetailArticleActivity

class ArticlesMoreAdapter(private val listArticles: List<Articles>) : RecyclerView.Adapter<ArticlesMoreAdapter.ArticleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ArticlesMoreItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = listArticles[position]
        holder.bind(article)
        holder.itemView.setOnClickListener { item ->
            val detailArticle = Intent(item.context, DetailArticleActivity::class.java)
            detailArticle.putExtra(DetailArticleActivity.EXTRA_ARTICLE, article)
            item.context.startActivity(detailArticle)
        }
    }

    override fun getItemCount(): Int {
        return listArticles.size
    }

    inner class ArticleViewHolder(private val binding: ArticlesMoreItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Articles) {
            binding.titleArticles.text = article.title
            binding.publishDate.text = article.publishDate
            Glide.with(binding.root)
                .load(article.image)
                .into(binding.imgArticles)
        }
    }
}