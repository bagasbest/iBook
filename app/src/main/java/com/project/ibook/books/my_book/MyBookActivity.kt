package com.project.ibook.books.my_book

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.project.ibook.books.write.WriteActivity
import com.project.ibook.databinding.ActivityMyBookBinding

class MyBookActivity : AppCompatActivity() {

    private var binding: ActivityMyBookBinding? = null
    private var adapter: MyBookAdapter? = null

    override fun onResume() {
        super.onResume()
        initRecyclerView()
        initViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyBookBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.imageButton?.setOnClickListener {
            onBackPressed()
        }

        binding?.newNovel?.setOnClickListener {
            startActivity(Intent(this, WriteActivity::class.java))
        }
    }


    private fun initRecyclerView() {
        binding?.rvMyNovel?.layoutManager = LinearLayoutManager(this)
        adapter = MyBookAdapter()
        binding?.rvMyNovel?.adapter = adapter
    }

    private fun initViewModel() {
        val viewModel = ViewModelProvider(this)[MyBookViewModel::class.java]
        val uid = FirebaseAuth.getInstance().currentUser!!.uid

        binding?.progressBar?.visibility = View.VISIBLE
        viewModel.setListBookByUid(uid)
        viewModel.getBook().observe(this) { bookList ->
            if (bookList.size > 0) {
                adapter?.setData(bookList)
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