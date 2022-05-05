package com.project.ibook.books.my_book

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.ibook.databinding.ActivityMyBookBabDetailBinding

class MyBookBabDetailActivity : AppCompatActivity() {

    private var binding: ActivityMyBookBabDetailBinding? = null
    private var myBookBabModel: MyBookBabModel? = null
    private var isReadMode = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyBookBabDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        myBookBabModel = intent.getParcelableExtra(EXTRA_DATA)
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        binding?.textView3?.text = myBookBabModel?.title
        if(intent.getStringExtra(WRITER_ID) == uid) {
            binding?.writerSide?.visibility = View.VISIBLE
            binding?.pratinjau?.visibility = View.VISIBLE
            binding?.title?.setText(myBookBabModel?.title)
            binding?.sinopsis?.setText(myBookBabModel?.description)
        } else {
            binding?.description?.visibility = View.VISIBLE
            binding?.description?.text = myBookBabModel?.description
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
        const val EXTRA_DATA = "data"
        const val BAB_LIST = "babList"
        const val NOVEL_ID = "novelId"
        const val WRITER_ID = "writerId"
    }
}