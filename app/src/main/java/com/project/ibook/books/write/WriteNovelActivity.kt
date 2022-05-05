package com.project.ibook.books.write

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.project.ibook.books.my_book.*
import com.project.ibook.databinding.ActivityWriteNovelBinding

class WriteNovelActivity : AppCompatActivity() {

    private var binding: ActivityWriteNovelBinding? = null
    private var model: MyBookModel? = null
    private var adapter: MyBookBabAdapter? = null


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteNovelBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        model = intent.getParcelableExtra(EXTRA_DATA)
        initRecyclerView()
        Glide.with(this)
            .load(model?.image)
            .into(binding!!.image)

        binding?.textView4?.text = model?.title
        binding?.genre?.text = "Genre: ${model?.genre}"
        binding?.synopsis?.text = model?.synopsis
        binding?.viewTime?.text = "${model?.viewTime}\nKali Dilihat"

        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        if(model?.writerUid == uid) {
            binding?.addNewBab?.visibility = View.VISIBLE
        }

        binding?.backButton?.setOnClickListener {
            onBackPressed()
        }

        binding?.addNewBab?.setOnClickListener {
            val intent = Intent(this, MyBookBabAddActivity::class.java)
            intent.putExtra(MyBookBabAddActivity.BAB_LIST, model?.babList)
            intent.putExtra(MyBookBabAddActivity.NOVEL_ID, model?.uid)
            startActivity(intent)
        }

    }

    @SuppressLint("SetTextI18n")
    private fun initRecyclerView() {
        if(model?.babList == null) {
            binding?.noData?.visibility = View.VISIBLE
        } else {
            binding?.rvBab?.layoutManager = LinearLayoutManager(this)
            binding?.bab?.text = "${model?.babList?.size}\nBab"
            adapter = MyBookBabAdapter(model?.babList!!, model?.uid, model?.writerUid)
            binding?.rvBab?.adapter = adapter
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        const val EXTRA_DATA = "data"
    }
}