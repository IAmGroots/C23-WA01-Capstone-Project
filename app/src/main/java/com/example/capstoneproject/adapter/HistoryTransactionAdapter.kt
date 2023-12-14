package com.example.capstoneproject.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.capstoneproject.R
import com.example.capstoneproject.data.di.Injection
import com.example.capstoneproject.databinding.HistoryItemsBinding
import com.example.capstoneproject.model.History

class HistoryTransactionAdapter(private val listItems: List<History>) : RecyclerView.Adapter<HistoryTransactionAdapter.ItemsViewHolder>() {

    val midtransRepository = Injection.provideRepository()
    private val db = Injection.provideDatabaseFirestore()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        val binding = HistoryItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        val history = listItems[position]
        holder.bind(history)
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> ViewType.FIRST_ITEM.ordinal
            listItems.size - 1 -> ViewType.LAST_ITEM.ordinal
            else -> ViewType.OTHER_ITEMS.ordinal
        }
    }

    inner class ItemsViewHolder(private val binding: HistoryItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(history: History) {
            binding.tvPlan.text = history.idService.name + " Package"
            binding.tvDate.text = history.statusTransaction.transactionTime
            binding.tvPrice.text = when (history.idService.price) {
                249000 -> "Rp 249.000"
                549000 -> "Rp 549.000"
                949000 -> "Rp 949.000"
                else -> ""
            }
            binding.tvSpeed.text = when (history.idService.speed) {
                50 -> "Speed up to 50mb/s"
                30 -> "Speed up to 30mb/s"
                15 -> "Speed up to 15mb/s"
                else -> ""
            }

            binding.txtCancel.text = when (history.statusTransaction.transactionStatus.toString()) {
                "settlement" -> "Success"
                "pending" -> "Cancel"
                else -> "Expired"
            }

            binding.btnCancel.backgroundTintList = when (history.statusTransaction.transactionStatus.toString()) {
                "settlement" -> ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.green))
                "pending" -> ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.yellow))
                else -> ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.red))
            }

            if (history.statusTransaction.transactionStatus.toString() == "pending") {
                binding.btnCancel.setOnClickListener {
                    midtransRepository.cancelTransaction(history.idOrder) { response ->
                        response.let {
                            if (response?.transactionStatus.toString() == "cancel") {
                                db.collection("transaction")
                                    .whereEqualTo("idOrder", history.idOrder)
                                    .get()
                                    .addOnSuccessListener { querySnapshot ->
                                        for (document in querySnapshot) {
                                            db.collection("transaction").document(document.id).update("status", "Cancel")
                                                .addOnSuccessListener {
                                                    Toast.makeText(binding.root.context, "Transaction Canceled", Toast.LENGTH_SHORT).show()
                                                }
                                        }
                                    }
                            }
                        }
                    }
                }
            }

            // set margin for first and last item, and then margin for separator each item
            val marginTopBottom = 8
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