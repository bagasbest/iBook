package com.project.ibook.books.other

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.ibook.books.my_book.add_edit_bab_novel.MyBookBabModel
import com.project.ibook.databinding.ActivityNovelReadBinding

class NovelReadActivity : AppCompatActivity() {

    private var binding: ActivityNovelReadBinding? = null
    private var babList = ArrayList<MyBookBabModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNovelReadBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        babList = intent.getParcelableArrayListExtra(BAB_LIST)!!
        var babNo = intent.getIntExtra(BAB_NO, 0)
        checkBabNumber(babNo)
        initView(babNo)

        binding?.prev?.setOnClickListener {
            babNo--
            initView(babNo)
            checkBabNumber(babNo)
        }

        binding?.next?.setOnClickListener {
            babNo++
            initView(babNo)
            checkBabNumber(babNo)
        }

        binding?.backButton?.setOnClickListener {
            onBackPressed()
        }
    }

    private fun checkBabNumber(babNo: Int) {
        when (babNo) {
            0 -> {
                binding?.prev?.isEnabled = false
                binding?.next?.isEnabled = true
            }
            babList.size-1 -> {
                binding?.prev?.isEnabled = true
                binding?.next?.isEnabled = false
            }
            else -> {
                binding?.prev?.isEnabled = true
                binding?.next?.isEnabled = true
            }
        }
    }

    private fun initView(babNo: Int) {
        binding?.textView3?.text = babList[babNo].title
        binding?.description?.text = babList[babNo].description
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        const val BAB_LIST = "babList"
        const val BAB_NO = "babNo"
    }
}