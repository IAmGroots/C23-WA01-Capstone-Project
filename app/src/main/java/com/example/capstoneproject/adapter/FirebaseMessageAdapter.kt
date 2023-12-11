package com.example.capstoneproject.adapter

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.capstoneproject.R
import com.example.capstoneproject.databinding.ItemMessageBinding
import com.example.capstoneproject.model.Message
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class FirebaseMessageAdapter(
    options: FirebaseRecyclerOptions<Message>,
    private val currentUserName: String?
) : FirebaseRecyclerAdapter<Message, FirebaseMessageAdapter.MessageViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_message, parent, false)
        val binding = ItemMessageBinding.bind(view)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MessageViewHolder,
        position: Int,
        model: Message
    ) {
        holder.bind(model)
    }

    inner class MessageViewHolder(private val binding: ItemMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Message) {
            if (currentUserName == item.name && item.name != null) {
                binding.messageLeft.visibility = View.GONE
                binding.messageRight.visibility = View.VISIBLE
                binding.tvMessageRight.text = item.text
//                setTextColor(item.name, binding.tvMessageRight)
                binding.tvMessengerRight.text = item.name
                Glide.with(itemView.context)
                    .load(item.photoUrl)
                    .circleCrop()
                    .into(binding.ivMessengerRight)
                if (item.timestamp != null) {
                    binding.tvTimestampRight.text = DateUtils.getRelativeTimeSpanString(item.timestamp)
                }
            } else {
                binding.messageRight.visibility = View.GONE
                binding.messageLeft.visibility = View.VISIBLE
                binding.tvMessage.text = item.text
//                setTextColor(item.name, binding.tvMessage)
                binding.tvMessenger.text = item.name
                Glide.with(itemView.context)
                    .load(item.photoUrl)
                    .circleCrop()
                    .into(binding.ivMessenger)
                if (item.timestamp != null) {
                    binding.tvTimestamp.text = DateUtils.getRelativeTimeSpanString(item.timestamp)
                }
            }


        }
        private fun setTextColor(userName: String?, textView: TextView) {
            if (currentUserName == userName && userName != null) {
                textView.setBackgroundResource(R.drawable.rounded_message_blue)
            } else {
                textView.setBackgroundResource(R.drawable.rounded_message_yellow)
            }
        }
    }
}