package com.project.ibook.books.other

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.ibook.books.my_book.add_edit_bab_novel.MyBookBabModel
import com.project.ibook.databinding.ActivityNovelReadBinding

class NovelReadActivity : AppCompatActivity() {

    private var binding: ActivityNovelReadBinding? = null
    private var model: MyBookBabModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNovelReadBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        model = intent.getParcelableExtra(EXTRA_DATA)
        binding?.textView3?.text = model?.title
        binding?.description?.text = model?.description

        binding?.backButton?.setOnClickListener {
            onBackPressed()
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