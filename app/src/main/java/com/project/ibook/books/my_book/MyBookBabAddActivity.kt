package com.project.ibook.books.my_book

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.project.ibook.databinding.ActivityMyBookBabAddBinding

class MyBookBabAddActivity : AppCompatActivity() {

    private var binding: ActivityMyBookBabAddBinding? = null
    private var babList: ArrayList<MyBookBabModel>? = null
    private var isReadMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyBookBabAddBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        babList = intent.getParcelableExtra(BAB_LIST)

        if(babList == null) {
            binding?.textView3?.text = "Menulis Bab 1"
        }


        binding?.saveDraft?.setOnClickListener {
            formValidation()
        }

        binding?.pratinjau?.setOnClickListener {
            if(!isReadMode) {
                binding?.writerSide?.visibility = View.GONE
                binding?.description?.visibility = View.VISIBLE
                isReadMode = true
                binding?.pratinjau?.text = "Kembali Ke Editor Mode"
            } else {
                binding?.writerSide?.visibility = View.VISIBLE
                binding?.description?.visibility = View.GONE
                isReadMode = false
                binding?.pratinjau?.text = "Pratinjau Bab"
            }
        }


        binding?.backButton?.setOnClickListener {
            onBackPressed()
        }
    }

    private fun formValidation() {
        val title = binding?.title?.text.toString().trim()
        val description = binding?.sinopsis?.text.toString().trim()

        when {
            title.isEmpty() -> {
                Toast.makeText(this, "Judul bab tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            }
            description.isEmpty() -> {
                Toast.makeText(this, "Isi bab tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "Berhasil menyimpan Draft", Toast.LENGTH_SHORT).show()
                binding?.textView3?.text = title
                binding?.description?.text = description
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        const val BAB_LIST = "babList"
        const val NOVEL_ID = "novelId"
    }
}