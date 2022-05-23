package com.project.ibook.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.project.ibook.books.other.anda_mungkin_suka.NovelModel5

class SearchViewModel  :ViewModel() {

    private val novelList = MutableLiveData<ArrayList<NovelModel5>>()
    private val listData = ArrayList<NovelModel5>()
    private val TAG = SearchViewModel::class.java.simpleName

    fun setNovel() {
        listData.clear()

        try {
            FirebaseFirestore.getInstance().collection("novel")
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
                        model.genre = document.data["genre"] as ArrayList<String> /* = java.util.ArrayList<kotlin.String> */
                        model.image = document.data["image"].toString()
                        model.viewTime = document.data["viewTime"] as Long
                        model.babList = document.toObject(NovelModel5::class.java).babList

                        listData.add(model)
                    }
                    novelList.postValue(listData)
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
        } catch (error: Exception) {
            error.printStackTrace()
        }
    }

    fun setNovelByQuery(executedQuery: String) {
        listData.clear()

        try {
            FirebaseFirestore.getInstance().collection("novel")
                .whereGreaterThanOrEqualTo("titleTemp", executedQuery)
                .whereLessThanOrEqualTo("titleTemp", executedQuery + '\uf8ff')
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
                        model.genre = document.data["genre"] as ArrayList<String>
                        model.image = document.data["image"].toString()
                        model.viewTime = document.data["viewTime"] as Long
                        model.babList = document.toObject(NovelModel5::class.java).babList

                        listData.add(model)
                    }
                    novelList.postValue(listData)
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
        } catch (error: Exception) {
            error.printStackTrace()
        }
    }


    fun setNovelById(novelId: String) {
        listData.clear()

        try {
            FirebaseFirestore.getInstance().collection("novel")
                .whereEqualTo("uid", novelId)
                .get()
                .addOnSuccessListener { documents ->
                    if(documents.size() > 0) {
                        for (document in documents) {
                            val model = NovelModel5()

                            model.title = document.data["title"].toString()
                            model.uid = document.data["uid"].toString()
                            model.synopsis = document.data["synopsis"].toString()
                            model.status = document.data["status"].toString()
                            model.writerName = document.data["writerName"].toString()
                            model.writerUid = document.data["writerUid"].toString()
                            model.genre = document.data["genre"] as ArrayList<String>
                            model.image = document.data["image"].toString()
                            model.viewTime = document.data["viewTime"] as Long
                            model.babList = document.toObject(NovelModel5::class.java).babList

                            listData.add(model)
                        }
                        novelList.postValue(listData)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
        } catch (error: Exception) {
            error.printStackTrace()
        }
    }


    fun getBook() : LiveData<ArrayList<NovelModel5>> {
        return novelList
    }

}