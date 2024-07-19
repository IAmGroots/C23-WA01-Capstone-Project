package com.example.capstoneproject.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.capstoneproject.R
import com.example.capstoneproject.data.response.ArticleItem
import com.example.capstoneproject.databinding.ArticlesMoreItemsBinding
import com.example.capstoneproject.ui.articles.detail_article.DetailArticleActivity

class ArticlesMoreAdapter(private val listArticles: List<ArticleItem?>?) : RecyclerView.Adapter<ArticlesMoreAdapter.ArticleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ArticlesMoreItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = listArticles?.get(position)
        holder.bind(article)
        holder.itemView.setOnClickListener { item ->
            val detailArticle = Intent(item.context, DetailArticleActivity::class.java)
            if (article != null) {
                detailArticle.putExtra(DetailArticleActivity.EXTRA_ARTICLE, article.id.toString())
//                Toast.makeText(item.context, article.id.toString(), Toast.LENGTH_SHORT).show()
            }
            item.context.startActivity(detailArticle)
        }
    }

    override fun getItemCount(): Int {
        return listArticles?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> ViewType.FIRST_ITEM.ordinal
            (listArticles?.size ?: 0) - 1 -> ViewType.LAST_ITEM.ordinal
            else -> ViewType.OTHER_ITEMS.ordinal
        }
    }

    inner class ArticleViewHolder(private val binding: ArticlesMoreItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: ArticleItem?) {
            if (article != null) {
                binding.titleArticles.text = article.title
            }
            binding.publishDate.text = "01 Januari 2023"
            Glide.with(binding.root)
                .load(R.drawable.example_image_article)
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