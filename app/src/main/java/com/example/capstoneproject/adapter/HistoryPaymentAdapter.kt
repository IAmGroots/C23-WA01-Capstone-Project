package com.example.capstoneproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.capstoneproject.databinding.ArticlesItemsBinding
import com.example.capstoneproject.databinding.HistoryPaymentItemsBinding
import com.example.capstoneproject.model.Articles
import com.example.capstoneproject.model.HistoryPayment

class HistoryPaymentAdapter (private val listHistoryPayment: List<HistoryPayment>) : RecyclerView.Adapter<HistoryPaymentAdapter.HistoryPaymentViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryPaymentAdapter.HistoryPaymentViewHolder {
        val binding = HistoryPaymentItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryPaymentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryPaymentAdapter.HistoryPaymentViewHolder, position: Int) {
        val historyPayment = listHistoryPayment[position]
        holder.bind(historyPayment)
    }

    override fun getItemCount(): Int {
        return listHistoryPayment.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> ArticlesAdapter.ViewType.FIRST_ITEM.ordinal
            listHistoryPayment.size - 1 -> ArticlesAdapter.ViewType.LAST_ITEM.ordinal
            else -> ArticlesAdapter.ViewType.OTHER_ITEMS.ordinal
        }
    }

    inner class HistoryPaymentViewHolder(private val binding: HistoryPaymentItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(historyPayment: HistoryPayment) {
            binding.tvInvoiceDate.text = historyPayment.invoiceDate
            binding.tvPayment.text = historyPayment.payment.toString()

            // set margin for first and last item, and then margin for separator each item
            val marginTopBottom = 16
            val marginSeparator = 6
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