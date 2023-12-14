package com.example.capstoneproject.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.capstoneproject.databinding.HotspotItemsBinding
import com.example.capstoneproject.model.Hotspot

class HotspotAdapter(private val listHotspot: MutableList<Hotspot>) :
    RecyclerView.Adapter<HotspotAdapter.HotspotViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotspotViewHolder {
        val binding =
            HotspotItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HotspotViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HotspotViewHolder, position: Int) {
        val hotspot = listHotspot[position]
        holder.bind(hotspot)
        holder.itemView.setOnClickListener { item ->
            val gmmIntentUri =
                Uri.parse("google.navigation:q=" + hotspot.lat + "," + hotspot.lon + "&mode=w")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            item.context.startActivity(mapIntent)
        }
    }

    override fun getItemCount(): Int {
        return listHotspot.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> ViewType.FIRST_ITEM.ordinal
            listHotspot.size - 1 -> ViewType.LAST_ITEM.ordinal
            else -> ViewType.OTHER_ITEMS.ordinal
        }
    }

    inner class HotspotViewHolder(private val binding: HotspotItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(hotspot: Hotspot) {
            binding.nameHotspot.text = hotspot.name
            binding.locationHotspot.text = hotspot.location
            Glide.with(binding.root)
                .load(hotspot.img)
                .into(binding.imgHotspot)

            binding.btnDetail.setOnClickListener { item ->
                val gmmIntentUri = Uri.parse("google.navigation:q=" + hotspot.lat + "," + hotspot.lon + "&mode=w")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                item.context.startActivity(mapIntent)
            }

            // set margin for first and last item, and then margin for separator each item
            val marginTopBottom = 0
            val marginSeparator = 4
            val density = binding.root.context.resources.displayMetrics.density
            val topBottom = (marginTopBottom * density).toInt()
            val separator = (marginSeparator * density).toInt()
            val layoutParams = binding.root.layoutParams as RecyclerView.LayoutParams

            when (getItemViewType(adapterPosition)) {
                ViewType.FIRST_ITEM.ordinal -> {
                    layoutParams.topMargin = topBottom
                    layoutParams.bottomMargin = separator
                }

                ViewType.LAST_ITEM.ordinal -> {
                    layoutParams.topMargin = separator
                    layoutParams.bottomMargin = topBottom
                }

                else -> {
                    layoutParams.topMargin = separator
                    layoutParams.bottomMargin = separator
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