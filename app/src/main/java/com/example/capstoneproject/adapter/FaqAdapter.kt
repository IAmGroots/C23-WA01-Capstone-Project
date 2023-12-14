package com.example.capstoneproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.capstoneproject.databinding.FaqItemsBinding
import com.example.capstoneproject.model.Faq

class FaqAdapter(private var listFaq: List<Faq>) :
    RecyclerView.Adapter<FaqAdapter.FaqViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqViewHolder {
        val binding = FaqItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FaqViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FaqViewHolder, position: Int) {
        val faq = listFaq[position]
        holder.bind(faq)
        holder.itemView.setOnClickListener {
            isAnyItemExpanded(position)
            faq.isExpandable = !faq.isExpandable
            notifyItemChanged(position , Unit)
        }
    }

    override fun getItemCount(): Int {
        return listFaq.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> ViewType.FIRST_ITEM.ordinal
            listFaq.size - 1 -> ViewType.LAST_ITEM.ordinal
            else -> ViewType.OTHER_ITEMS.ordinal
        }
    }

    inner class FaqViewHolder(private val binding: FaqItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun collapseExpandedView(){
            binding.tvAnswers.visibility = View.GONE
        }

        fun bind(faq: Faq) {
            binding.tvQuestions.text = faq.question
            binding.tvAnswers.text = faq.answer
            binding.tvAnswers.visibility = if (faq.isExpandable) View.VISIBLE else View.GONE

            // set margin for first and last item, and then margin for separator each item
            val marginTopBottom = 16
            val marginSeparator = 8
            val density = binding.root.context.resources.displayMetrics.density
            val topBottom = (marginTopBottom * density).toInt()
            val separator = (marginSeparator * density).toInt()
            val layoutParams = binding.root.layoutParams as RecyclerView.LayoutParams

            when (getItemViewType(adapterPosition)) {
                HistoryTransactionAdapter.ViewType.FIRST_ITEM.ordinal -> {
                    layoutParams.topMargin = topBottom
                    layoutParams.bottomMargin = separator
                }
                HistoryTransactionAdapter.ViewType.LAST_ITEM.ordinal -> {
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

    private fun isAnyItemExpanded(position: Int){
        val temp = listFaq.indexOfFirst {
            it.isExpandable
        }
        if (temp >= 0 && temp != position){
            listFaq[temp].isExpandable = false
            notifyItemChanged(temp , 0)
        }
    }

    override fun onBindViewHolder(
        holder: FaqViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {

        if(payloads.isNotEmpty() && payloads[0] == 0){
            holder.collapseExpandedView()
        }else{
            super.onBindViewHolder(holder, position, payloads)

        }
    }

    enum class ViewType {
        FIRST_ITEM,
        LAST_ITEM,
        OTHER_ITEMS
    }
}