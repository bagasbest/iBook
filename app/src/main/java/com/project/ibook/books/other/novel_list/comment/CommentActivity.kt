package com.project.ibook.books.other.novel_list.comment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.ibook.databinding.ActivityCommentBinding
import java.text.SimpleDateFormat
import java.util.*

class CommentActivity : AppCompatActivity() {

    private var binding: ActivityCommentBinding? = null
    private var username: String? = null
    private var adapter: CommentAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        initRecyclerView()
        initViewModel()
        getUsername()

        binding?.sendBtn?.setOnClickListener {
            formValidation()
        }

        binding?.backButton?.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initRecyclerView() {

            val layoutManager = LinearLayoutManager(this)
            binding?.rvComment?.layoutManager = layoutManager
            adapter = CommentAdapter()
            binding?.rvComment?.adapter = adapter

    }

    private fun initViewModel() {
        val viewModel = ViewModelProvider(this)[CommentViewModel::class.java]
        binding?.progressBar?.visibility = View.VISIBLE
        val novelId = intent.getStringExtra(NOVEL_ID)
        viewModel.setListComment(novelId!!)
        viewModel.getComment().observe(this) { commentList ->
            if (commentList.size > 0) {
                binding?.noData?.visibility = View.GONE
                adapter?.setData(commentList)
            } else {
                binding?.noData?.visibility = View.VISIBLE
            }
            binding?.progressBar?.visibility = View.GONE
        }
    }

    private fun getUsername() {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        FirebaseFirestore
            .getInstance()
            .collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener {
                username = "" + it.data!!["username"]
            }
    }

    private fun formValidation() {
        val comment = binding?.commentEt?.text.toString().trim()
        if (comment.isEmpty()) {
            Toast.makeText(this, "Komentar tidak boleh kosong", Toast.LENGTH_SHORT).show()
        } else {

            val uid = System.currentTimeMillis().toString()

            val c = Calendar.getInstance()
            val df = SimpleDateFormat("dd-mm-yyyy, HH:mm", Locale.getDefault())
            val formattedDate = df.format(c.time)
            val novelId = intent.getStringExtra(NOVEL_ID)

            val data = mapOf(
                "uid" to uid,
                "comment" to comment,
                "username" to username,
                "date" to formattedDate
            )

            FirebaseFirestore
                .getInstance()
                .collection("novel")
                .document(novelId!!)
                .collection("comment")
                .document(uid)
                .set(data)
                .addOnCompleteListener {
                    if(it.isSuccessful) {
                        initRecyclerView()
                        initViewModel()
                        binding?.commentEt.setText("")
                        Toast.makeText(this, "Berhasil menulis komentar!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Gagal menulis komentar!", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        const val NOVEL_ID = "id"
    }
}