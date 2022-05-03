package com.project.ibook.books.write

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.ibook.databinding.ActivityWriteBinding

class WriteActivity : AppCompatActivity() {

    private var binding: ActivityWriteBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.backButton?.setOnClickListener {
            onBackPressed()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}