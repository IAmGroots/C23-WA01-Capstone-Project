package com.example.capstoneproject.ui.order

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.capstoneproject.data.di.Injection
import com.example.capstoneproject.model.Package
import com.google.firebase.firestore.Query

class OrderViewModel : ViewModel() {

    private val db = Injection.provideDatabaseFirestore()

    private val _isTransactionAllowed = MutableLiveData<Boolean>()
    val isTransactionAllowed: LiveData<Boolean> = _isTransactionAllowed

    fun checkLastTransaction(userID: String) {
        db.collection("transaction")
            .whereEqualTo("idUser", userID)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val lastTransaction =
                    querySnapshot.sortedByDescending { it.get("timestamp").toString() }
                        .firstOrNull()
                if (lastTransaction != null) {
                    val status = lastTransaction.get("status").toString()
                    _isTransactionAllowed.value = status != "Pending"
                } else {
                    _isTransactionAllowed.value = true
                }
            }
    }
}

