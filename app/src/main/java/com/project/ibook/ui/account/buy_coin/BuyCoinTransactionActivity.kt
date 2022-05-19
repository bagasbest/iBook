package com.project.ibook.ui.account.buy_coin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.ibook.databinding.ActivityBuyCoinTransactionBinding
import com.project.ibook.ui.account.buy_coin.transaction.TransactionAdapter
import com.project.ibook.ui.account.buy_coin.transaction.TransactionViewModel

class BuyCoinTransactionActivity : AppCompatActivity() {

    private var binding : ActivityBuyCoinTransactionBinding? = null
    private var adapter: TransactionAdapter? = null

    override fun onResume() {
        super.onResume()
        checkRole()
    }

    private fun checkRole() {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        FirebaseFirestore
            .getInstance()
            .collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener {
                if("" + it.data!!["role"] == "admin") {
                    initRecyclerView()
                    initViewModel("admin")
                } else {
                    initRecyclerView()
                    initViewModel("user")
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuyCoinTransactionBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.backButton?.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        binding?.rvTransaction?.layoutManager = layoutManager
        adapter = TransactionAdapter()
        binding?.rvTransaction?.adapter = adapter
    }

    private fun initViewModel(role: String) {
        val viewModel = ViewModelProvider(this)[TransactionViewModel::class.java]

        binding?.progressBar?.visibility = View.VISIBLE
        if(role == "admin") {
            viewModel.setListTransactionAll()
        } else {
            val uid = FirebaseAuth.getInstance().currentUser!!.uid
            viewModel.setListTransactionById(uid)
        }
        viewModel.getTransaction().observe(this) { transactionList ->
            if (transactionList.size > 0) {
                adapter?.setData(transactionList)
                binding?.noData?.visibility = View.GONE
            } else {
                binding?.noData?.visibility = View.VISIBLE
            }
            binding!!.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}