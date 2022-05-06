package com.project.ibook.books.my_book

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class MyBookViewModel  : ViewModel() {

    private val bookList = MutableLiveData<ArrayList<MyBookModel>>()
    private val listData = ArrayList<MyBookModel>()
    private val TAG = MyBookViewModel::class.java.simpleName


    fun setListBookByUid(uid: String) {
        listData.clear()

        try {
            FirebaseFirestore.getInstance().collection("novel")
                .whereEqualTo("writerUid", uid)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val model = MyBookModel()

                        model.title = document.data["title"].toString()
                        model.uid = document.data["uid"].toString()
                        model.synopsis = document.data["synopsis"].toString()
                        model.status = document.data["status"].toString()
                        model.writerName = document.data["writerName"].toString()
                        model.writerUid = document.data["writerUid"].toString()
                        model.genre = document.data["genre"].toString()
                        model.image = document.data["image"].toString()
                        model.viewTime = document.data["viewTime"] as Long
                        model.babList = document.toObject(MyBookModel::class.java).babList

                        listData.add(model)
                    }
                    bookList.postValue(listData)
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
        } catch (error: Exception) {
            error.printStackTrace()
        }
    }


    fun getBook() : LiveData<ArrayList<MyBookModel>> {
        return bookList
    }

}