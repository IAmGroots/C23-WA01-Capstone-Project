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

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> ViewType.FIRST_ITEM.ordinal
            listArticles.size - 1 -> ViewType.LAST_ITEM.ordinal
            else -> ViewType.OTHER_ITEMS.ordinal
        }
    }

    inner class ArticleViewHolder(private val binding: ArticlesMoreItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Articles) {
            binding.titleArticles.text = article.title
            binding.publishDate.text = article.publishDate
            Glide.with(binding.root)
                .load(article.image)
                .into(binding.imgArticles)

            // set margin for first and last item, and then margin for separator each item
            val marginStartEnd = 0
            val marginSeparator = 6
            val density = binding.root.context.resources.displayMetrics.density
            val startEnd = (marginStartEnd * density).toInt()
            val separator = (marginSeparator * density).toInt()
            val layoutParams = binding.root.layoutParams as RecyclerView.LayoutParams

            when (getItemViewType(adapterPosition)) {
                ViewType.FIRST_ITEM.ordinal -> {
                    layoutParams.marginStart = startEnd
                    layoutParams.marginEnd = separator
                }
                ViewType.LAST_ITEM.ordinal -> {
                    layoutParams.marginStart = separator
                    layoutParams.marginEnd = startEnd
                }
                else -> {
                    layoutParams.marginStart = separator
                    layoutParams.marginEnd = separator
                }
            }

            binding.root.layoutParams = layoutParams
        }
    }

    enum class ViewType {
        FIRST_ITEM,
        LAST_ITEM,
        OTHER_ITEMS
    }
}