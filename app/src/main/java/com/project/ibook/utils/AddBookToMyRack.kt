package com.project.ibook.utils

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object AddBookToMyRack {

    fun addBook(novelId: String?, context: Context, option: String) {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val data = mapOf(
            "uid" to novelId
        )

        FirebaseFirestore
            .getInstance()
            .collection("users")
            .document(uid)
            .collection("rak_buku")
            .document(novelId!!)
            .set(data)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    if (option == "add") {
                        Toast.makeText(
                            context,
                            "Berhasil menambahkan buku kedalam rak buku!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    if (option == "add") {
                        Toast.makeText(
                            context,
                            "Gagal menambahkan buku kedalam rak buku!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
    }

    fun deleteRack(novelId: String?, context: Context) {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        FirebaseFirestore
            .getInstance()
            .collection("users")
            .document(uid)
            .collection("rak_buku")
            .document(novelId!!)
            .delete()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(
                        context,
                        "Berhasil menghapus buku ini dari rak buku!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        "Gagal menghapus buku ini dari rak buku!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}