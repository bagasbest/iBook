package com.project.ibook.books.other.terlaris

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class NovelViewModel1 : ViewModel() {

    private val bookList = MutableLiveData<ArrayList<NovelModel1>>()
    private val listData = ArrayList<NovelModel1>()
    private val TAG = NovelViewModel1::class.java.simpleName


    fun setListBookByFamousAll() {
        listData.clear()

        try {
            FirebaseFirestore.getInstance().collection("novel")
                .whereEqualTo("status", "Published")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val model = NovelModel1()

                        model.title = document.data["title"].toString()
                        model.uid = document.data["uid"].toString()
                        model.synopsis = document.data["synopsis"].toString()
                        model.status = document.data["status"].toString()
                        model.titleTemp = document.data["titleTemp"].toString()
                        model.homepageCategory = document.data["homepageCategory"].toString()
                        model.writerName = document.data["writerName"].toString()
                        model.writerUid = document.data["writerUid"].toString()
                        model.genre = document.data["genre"] as ArrayList<String>
                        model.image = document.data["image"].toString()
                        model.viewTime = document.data["viewTime"] as Long
                        model.coins = document.data["coins"] as Long
                        model.babList = document.toObject(NovelModel1::class.java).babList

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

    fun setListBookByFamousLimited() {
        listData.clear()

        try {
            FirebaseFirestore.getInstance().collection("novel")
                .whereEqualTo("status", "Published")
                .limit(50)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val model = NovelModel1()

                        model.title = document.data["title"].toString()
                        model.uid = document.data["uid"].toString()
                        model.synopsis = document.data["synopsis"].toString()
                        model.titleTemp = document.data["titleTemp"].toString()
                        model.homepageCategory = document.data["homepageCategory"].toString()
                        model.status = document.data["status"].toString()
                        model.writerName = document.data["writerName"].toString()
                        model.writerUid = document.data["writerUid"].toString()
                        model.genre = document.data["genre"] as ArrayList<String>
                        model.image = document.data["image"].toString()
                        model.viewTime = document.data["viewTime"] as Long
                        model.coins = document.data["coins"] as Long
                        model.babList = document.toObject(NovelModel1::class.java).babList

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


    fun setListMyBookRack(uid: String) {
        listData.clear()

        try {
            FirebaseFirestore.getInstance().collection("users")
                .document(uid)
                .collection("rak_buku")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val model = NovelModel1()

                        model.uid = document.data["uid"].toString()

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


    fun getBook() : LiveData<ArrayList<NovelModel1>> {
        return bookList
    }

}