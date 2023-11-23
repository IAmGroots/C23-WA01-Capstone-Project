package com.example.capstoneproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.capstoneproject.databinding.BannerItemsBinding
import com.example.capstoneproject.model.Banner

class BannerAdapter(private val listBanner: List<Banner>) : RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

    inner class BannerViewHolder(private val binding: BannerItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(banner: Banner) {
            Glide.with(binding.root)
                .load(banner.url)
                .into(binding.imgBanner)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val binding = BannerItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BannerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        val banner = listBanner[position]
        holder.bind(banner)
        // jika mau menavigasikan ke halaman lain
//        holder.itemView.setOnClickListener { item ->
//            Toast.makeText(item.context, "${banner.title} Clicked", Toast.LENGTH_SHORT).show()
//        }
    }

    override fun getItemCount(): Int {
        return listBanner.size
    }
}