package com.project.ibook.ui.account.buy_coin.transaction

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class TransactionViewModel : ViewModel() {

    private val transactionList = MutableLiveData<ArrayList<TransactionModel>>()
    private val listData = ArrayList<TransactionModel>()
    private val TAG = TransactionViewModel::class.java.simpleName


    fun setListTransactionById(uid: String) {
        listData.clear()

        try {
            FirebaseFirestore.getInstance().collection("transaction")
                .whereEqualTo("userId", uid)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val model = TransactionModel()

                        model.coin = document.data["coin"] as Long
                        model.uid = document.data["uid"].toString()
                        model.date = document.data["date"].toString()
                        model.status = document.data["status"].toString()
                        model.paymentProof = document.data["paymentProof"].toString()
                        model.paymentMethod = document.data["paymentMethod"].toString()
                        model.price = document.data["price"].toString()
                        model.userId = document.data["userId"].toString()
                        model.username = document.data["username"].toString()

                        listData.add(model)
                    }
                    transactionList.postValue(listData)
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
        } catch (error: Exception) {
            error.printStackTrace()
        }
    }


    fun setListTransactionAll() {
        listData.clear()

        try {
            FirebaseFirestore.getInstance().collection("transaction")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val model = TransactionModel()

                        model.coin = document.data["coin"] as Long
                        model.uid = document.data["uid"].toString()
                        model.date = document.data["date"].toString()
                        model.status = document.data["status"].toString()
                        model.paymentProof = document.data["paymentProof"].toString()
                        model.paymentMethod = document.data["paymentMethod"].toString()
                        model.price = document.data["price"].toString()
                        model.userId = document.data["userId"].toString()
                        model.username = document.data["username"].toString()

                        listData.add(model)
                    }
                    transactionList.postValue(listData)
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
        } catch (error: Exception) {
            error.printStackTrace()
        }
    }


    fun getTransaction() : LiveData<ArrayList<TransactionModel>> {
        return transactionList
    }


}