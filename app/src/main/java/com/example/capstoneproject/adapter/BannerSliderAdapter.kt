package com.example.capstoneproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.capstoneproject.R
import com.smarteist.autoimageslider.SliderViewAdapter

class BannerSliderAdapter(sliders: ArrayList<BannerSliderItem>) : SliderViewAdapter<BannerSliderAdapter.SliderViewHolder>() {

    var sliderList: ArrayList<BannerSliderItem> = sliders

    override fun getCount(): Int {
        return sliderList.size
    }
    override fun onCreateViewHolder(parent: ViewGroup?): BannerSliderAdapter.SliderViewHolder {
        val inflate: View =
            LayoutInflater.from(parent!!.context).inflate(R.layout.banner_slider_item, null)
        return SliderViewHolder(inflate)
    }

    override fun onBindViewHolder(viewHolder: SliderViewHolder, position: Int) {
        Glide.with(viewHolder.itemView)
            .load(sliderList[position].image)
            .into(viewHolder.imageView)
    }

    class SliderViewHolder(itemView: View) : SliderViewAdapter.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.banner_slider_view)
    }
}