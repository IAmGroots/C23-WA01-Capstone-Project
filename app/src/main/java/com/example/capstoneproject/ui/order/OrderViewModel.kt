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

    fun checkLastTransaction(uid: String) {
        db.collection("transaction")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val idUser = document.get("idUser").toString()
                    val status = document.get("status").toString()
                    if (idUser == uid) {
                        _isTransactionAllowed.value = status != "Pending"
                    }
                    break
                }
            }
    }

}