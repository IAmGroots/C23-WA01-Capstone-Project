package com.example.capstoneproject.adapter

import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.capstoneproject.R
import com.example.capstoneproject.data.di.Injection
import com.example.capstoneproject.databinding.ItemMessageBinding
import com.example.capstoneproject.model.Message
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class FirebaseMessageAdapter(
    options: FirebaseRecyclerOptions<Message>,
    private val currentUserID: String?
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
        private val db = Injection.provideDatabaseFirestore()
        fun bind(item: Message) {
            // jika chat user saat ini
            if (currentUserID == item.id && item.id != null) {
                getFullnameFromDB(currentUserID.toString()) { fullname ->
                    if (fullname != null) {
                        binding.tvMessengerRight.text = fullname
                    } else {
                        Log.d("FirebaseMessageAdapter", "Something went wrong")
                    }
                }
                binding.messageLeft.visibility = View.GONE
                binding.messageRight.visibility = View.VISIBLE
                binding.tvMessageRight.text = item.text
                Glide.with(itemView.context)
                    .load(item.photoUrl)
                    .circleCrop()
                    .into(binding.ivMessengerRight)
                if (item.timestamp != null) {
                    binding.tvTimestampRight.text = DateUtils.getRelativeTimeSpanString(item.timestamp)
                }
            }
            // jika chat admin
            else {
                binding.messageRight.visibility = View.GONE
                binding.messageLeft.visibility = View.VISIBLE
                binding.tvMessage.text = item.text
                binding.tvMessenger.text = item.id
                Glide.with(itemView.context)
                    .load(item.photoUrl)
                    .circleCrop()
                    .into(binding.ivMessenger)
                if (item.timestamp != null) {
                    binding.tvTimestamp.text = DateUtils.getRelativeTimeSpanString(item.timestamp)
                }
            }
        }

        private fun getFullnameFromDB(idUser: String, callback: (String?) -> Unit) {
            db.collection("user")
                .whereEqualTo("uid", idUser)
                .limit(1)
                .get()
                .addOnSuccessListener { data ->
                    if (!data.isEmpty) {
                        val userDocument = data.documents[0]
                        val firstname = userDocument.get("firstname").toString()
                        val lastname = userDocument.get("lastname").toString()
                        val fullname = "$firstname $lastname"
                        callback.invoke(fullname)
                    } else {
                        Log.d("FirebaseMessageAdapter", "Something went wrong")
                        callback.invoke(null)
                    }
                }
        }
    }
}