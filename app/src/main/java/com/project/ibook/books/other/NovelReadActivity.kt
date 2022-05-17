package com.project.ibook.books.other

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import com.project.ibook.books.my_book.add_edit_bab_novel.MyBookBabModel
import com.project.ibook.databinding.ActivityNovelReadBinding

class NovelReadActivity : AppCompatActivity() {

    private var binding: ActivityNovelReadBinding? = null
    private var babList = ArrayList<MyBookBabModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNovelReadBinding.inflate(layoutInflater)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
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
        Log.e("tag", babNo.toString())
        Log.e("tag", babList.size.toString())
        if(babNo == 0 && babList.size > 1) {
            binding?.prev?.isEnabled = false
            binding?.next?.isEnabled = true
        } else if (babList.size == 1 ) {
            binding?.prev?.isEnabled = false
            binding?.next?.isEnabled = false
        } else if (babNo == babList.size-1) {
            binding?.prev?.isEnabled = true
            binding?.next?.isEnabled = false
        } else {
            binding?.prev?.isEnabled = true
            binding?.next?.isEnabled = true
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