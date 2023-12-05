package com.example.capstoneproject.ui.history.unsuccessful

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstoneproject.R
import com.example.capstoneproject.adapter.HistoryTransactionAdapter
import com.example.capstoneproject.databinding.FragmentUnsuccessfulBinding
import com.example.capstoneproject.model.History
import com.example.capstoneproject.model.Order
import com.example.capstoneproject.ui.history.HistoryViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class UnsuccessfulFragment : Fragment() {

    private var _binding: FragmentUnsuccessfulBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HistoryViewModel
    private val db = Firebase.firestore
    private val id_user = "1701"
    private var listOrder: MutableList<Order> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentUnsuccessfulBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel = ViewModelProvider(this)[HistoryViewModel::class.java]

        getHistoryTransaction(id_user)

        viewModel.isLoading.observe(requireActivity()) {
            showLoading(it)
        }

        binding.swipeRefresh.setOnRefreshListener {
            getHistoryTransaction(id_user)
            Toast.makeText(requireContext(), "Berhasil Refresh", Toast.LENGTH_SHORT).show()
            binding.swipeRefresh.isRefreshing = false
        }

        return root
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun getHistoryTransaction(idUser: String) {
        listOrder.clear()
        db.collection("transaction")
            .whereEqualTo("idUser", idUser)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val idUser = document.get("idUser").toString()
                    val idOrder = document.get("idOrder").toString()
                    val service = viewModel.getService(document.get("idService").toString().toInt())
                    service?.let {
                        listOrder.add(
                            Order(
                                idOrder,
                                idUser,
                                service
                            )
                        )
                    }
                    Log.d("MainViewModel", "Berhasil Disini getHistoryTransaction Activity")
                }
                viewModel.getAllHistoryTransactionUnsuccessful(listOrder)
                Log.d("MainViewModel", "getHistoryTransaction Activity")
                viewModel.listHistory.observe(requireActivity()) { listHistory ->
                    setRecylerview(listHistory)
                }
                Log.d("MainViewModel", "setRecyclerView Activity")
            }
            .addOnFailureListener {
                Log.d("HISTORY", "Something went wrong")
            }
    }

    private fun setRecylerview(listHistory: List<History>) {
        val sortedList = listHistory.sortedByDescending { it.statusTransaction.transactionTime }
        if (sortedList.isEmpty()) {
            binding.rvHistory.visibility = View.GONE
            binding.tvEmpty.visibility = View.VISIBLE
        } else {
            binding.tvEmpty.visibility = View.GONE
            binding.rvHistory.visibility = View.VISIBLE
            binding.rvHistory.setHasFixedSize(true)
            binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())
            binding.rvHistory.adapter = HistoryTransactionAdapter(sortedList)
        }
    }
}