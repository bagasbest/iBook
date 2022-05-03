package com.project.ibook.books.my_book

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.ibook.books.write.WriteActivity
import com.project.ibook.databinding.ActivityMyBookBinding

class MyBookActivity : AppCompatActivity() {

    private var binding: ActivityMyBookBinding? = null

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

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}