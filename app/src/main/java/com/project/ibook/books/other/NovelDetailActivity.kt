package com.project.ibook.books.other

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.project.ibook.books.my_book.MyBookBabModel
import com.project.ibook.databinding.ActivityNovelDetailBinding

class NovelDetailActivity : AppCompatActivity() {

    private var binding: ActivityNovelDetailBinding? = null
    private var model5: NovelModel5? = null
    private var adapter: NovelReadAdapter? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNovelDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val option = intent.getStringExtra(OPTION)
        when (option) {
            "1" -> {

            }
            "2" -> {

            }
            "3" -> {

            }
            "4" -> {

            }
            else -> {
                model5 = intent.getParcelableExtra(EXTRA_DATA)
                initRecyclerView5()
                Glide.with(this)
                    .load(model5?.image)
                    .into(binding!!.image)

                binding?.textView4?.text = model5?.title
                binding?.genre?.text = "Genre: ${model5?.genre}"
                binding?.synopsis?.text = model5?.synopsis
                binding?.viewTime?.text = "${model5?.viewTime}\nKali Dilihat"
                getWordCountNovel(model5?.babList!!)
            }
        }

        binding?.backButton?.setOnClickListener {
            onBackPressed()
        }

    }

    @SuppressLint("SetTextI18n")
    private fun getWordCountNovel(babList: ArrayList<MyBookBabModel>) {
        var wordCount = 0L
        for(i in babList.indices) {
            val description = babList[i].description.toString().trim()
            wordCount += description.split("\\s+".toRegex()).size
        }

        binding?.wordCount?.text = "$wordCount\nKata"
    }

    @SuppressLint("SetTextI18n")
    private fun initRecyclerView5() {
        if(model5?.babList == null) {
            binding?.noData?.visibility = View.VISIBLE
        } else {
            binding?.rvBab?.layoutManager = LinearLayoutManager(this)
            binding?.bab?.text = "${model5?.babList?.size}\nBab"
            adapter = NovelReadAdapter(model5?.babList!!)
            binding?.rvBab?.adapter = adapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        const val EXTRA_DATA = "data"
        const val OPTION = "option"
    }
}