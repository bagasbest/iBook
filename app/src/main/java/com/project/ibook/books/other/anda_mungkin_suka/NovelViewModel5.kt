package com.project.ibook.books.other.anda_mungkin_suka

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class NovelViewModel5 : ViewModel() {

    private val bookList = MutableLiveData<ArrayList<NovelModel5>>()
    private val listData = ArrayList<NovelModel5>()
    private val TAG = NovelViewModel5::class.java.simpleName


    fun setListBookAll() {
        listData.clear()

        try {
            FirebaseFirestore.getInstance().collection("novel")
                .whereEqualTo("status", "Published")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val model = NovelModel5()

                        model.title = document.data["title"].toString()
                        model.uid = document.data["uid"].toString()
                        model.synopsis = document.data["synopsis"].toString()
                        model.status = document.data["status"].toString()
                        model.writerName = document.data["writerName"].toString()
                        model.writerUid = document.data["writerUid"].toString()
                        model.genre = document.data["genre"].toString()
                        model.image = document.data["image"].toString()
                        model.viewTime = document.data["viewTime"] as Long
                        model.babList = document.toObject(NovelModel5::class.java).babList

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

    fun setListBookLimited() {
        listData.clear()

        try {
            FirebaseFirestore.getInstance().collection("novel")
                .whereEqualTo("status", "Published")
                .limit(50)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val model = NovelModel5()

                        model.title = document.data["title"].toString()
                        model.uid = document.data["uid"].toString()
                        model.synopsis = document.data["synopsis"].toString()
                        model.status = document.data["status"].toString()
                        model.writerName = document.data["writerName"].toString()
                        model.writerUid = document.data["writerUid"].toString()
                        model.genre = document.data["genre"].toString()
                        model.image = document.data["image"].toString()
                        model.viewTime = document.data["viewTime"] as Long
                        model.babList = document.toObject(NovelModel5::class.java).babList

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


    fun getBook() : LiveData<ArrayList<NovelModel5>> {
        return bookList
    }


}