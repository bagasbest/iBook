package com.project.ibook.books.other.pilihan_iBook

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class NovelViewModel2 : ViewModel() {

    private val bookList = MutableLiveData<ArrayList<NovelModel2>>()
    private val listData = ArrayList<NovelModel2>()
    private val TAG = NovelViewModel2::class.java.simpleName


    fun setListBookByiBookChoiceAll() {
        listData.clear()

        try {
            FirebaseFirestore.getInstance().collection("novel")
                .whereEqualTo("status", "Published")
                .whereEqualTo("homepageCategory", "Pilihan KoalaNovel")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val model = NovelModel2()

                        model.title = document.data["title"].toString()
                        model.uid = document.data["uid"].toString()
                        model.synopsis = document.data["synopsis"].toString()
                        model.status = document.data["status"].toString()
                        model.writerName = document.data["writerName"].toString()
                        model.writerUid = document.data["writerUid"].toString()
                        model.genre = document.data["genre"] as ArrayList<String>
                        model.image = document.data["image"].toString()
                        model.viewTime = document.data["viewTime"] as Long
                        model.coins = document.data["coins"] as Long
                        model.babList = document.toObject(NovelModel2::class.java).babList

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

    fun setListBookByiBookChoiceLimited() {
        listData.clear()

        try {
            FirebaseFirestore.getInstance().collection("novel")
                .whereEqualTo("status", "Published")
                .whereEqualTo("homepageCategory", "Pilihan KoalaNovel")
                .limit(50)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val model = NovelModel2()

                        model.title = document.data["title"].toString()
                        model.uid = document.data["uid"].toString()
                        model.synopsis = document.data["synopsis"].toString()
                        model.status = document.data["status"].toString()
                        model.writerName = document.data["writerName"].toString()
                        model.writerUid = document.data["writerUid"].toString()
                        model.genre = document.data["genre"] as ArrayList<String>
                        model.image = document.data["image"].toString()
                        model.viewTime = document.data["viewTime"] as Long
                        model.coins = document.data["coins"] as Long
                        model.babList = document.toObject(NovelModel2::class.java).babList

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


    fun getBook() : LiveData<ArrayList<NovelModel2>> {
        return bookList
    }


}