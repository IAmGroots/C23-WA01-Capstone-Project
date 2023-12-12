package com.example.capstoneproject.ui.chat

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstoneproject.R
import com.example.capstoneproject.adapter.FirebaseMessageAdapter
import com.example.capstoneproject.databinding.ActivityChatBinding
import com.example.capstoneproject.model.Message
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Date

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var dbRealtime: FirebaseDatabase
    private val db = Firebase.firestore
    private lateinit var adapter: FirebaseMessageAdapter
    private val MESSAGES_CHILD = "messages"
    private val userID = FirebaseAuth.getInstance().currentUser!!.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setFocusable()

        dbRealtime = Firebase.database
        val messagesRef = dbRealtime.reference.child(MESSAGES_CHILD).child(userID)

        val manager = LinearLayoutManager(this)
        manager.stackFromEnd = true
        binding.messageRecyclerView.layoutManager = manager

        val options = FirebaseRecyclerOptions.Builder<Message>()
            .setQuery(messagesRef, Message::class.java)
            .build()

        var fullname = intent.getStringExtra(EXTRA_FULLNAME)
        adapter = FirebaseMessageAdapter(options, fullname)
        binding.messageRecyclerView.adapter = adapter

        binding.sendButton.setOnClickListener {
            val friendlyMessage = Message(
                binding.messageEditText.text.toString(),
                fullname,
                "https://assets-global.website-files.com/5b6a5372c97f7911bd2e0867/5db6209732df59a78a669e25_image-p-500.png",
                Date().time
            )
            messagesRef.push().setValue(friendlyMessage) { error, _ ->
                if (error != null) {
                    Toast.makeText(this, "Gagal terkirim", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Berhasil terkirim", Toast.LENGTH_SHORT).show()
                }
            }
            binding.messageEditText.setText("")
        }

    }

    public override fun onResume() {
        super.onResume()
        adapter.startListening()
    }
    public override fun onPause() {
        adapter.stopListening()
        super.onPause()
    }

    private fun setupToolbar() {
        val toolbar = binding.toolbar

        toolbar.setNavigationOnClickListener {
            super.onBackPressed()
        }
    }

    companion object {
        const val EXTRA_FULLNAME = "extra_fullname"
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setFocusable() {
        binding.messageEditText.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    binding.messageEditText.isFocusable = true
                    binding.messageEditText.isFocusableInTouchMode = true
                }

                MotionEvent.ACTION_UP -> {
                    binding.messageEditText.requestFocus()
                }
            }
            false
        }

    }
}