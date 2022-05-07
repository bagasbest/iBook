package com.project.ibook.books.other

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.ibook.databinding.ActivityNovelListBinding

class NovelListActivity : AppCompatActivity() {

    private var binding: ActivityNovelListBinding? = null
    private var novelList1 = ArrayList<NovelModel1>()
    private var novelList2 = ArrayList<NovelModel2>()
    private var novelList3 = ArrayList<NovelModel3>()
    private var novelList4 = ArrayList<NovelModel4>()
    private var novelList5 = ArrayList<NovelModel5>()
    private var adapter: NovelAdapter? = null
    private var option: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNovelListBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        option = intent.getStringExtra(OPTION)
        binding?.textView3?.text = intent.getStringExtra(TITLE)
        if(option == "1") {
            novelList1 = intent.getParcelableArrayListExtra(EXTRA_DATA)!!
            if(novelList1.size == 0) {
                binding?.noData?.visibility = View.VISIBLE
            } else {
                initRecyclerView()
            }
        } else if (option == "2") {
            novelList2 = intent.getParcelableArrayListExtra(EXTRA_DATA)!!
            if(novelList2.size == 0) {
                binding?.noData?.visibility = View.VISIBLE
            } else {
                initRecyclerView()
            }
        } else if (option == "3") {
            novelList3 = intent.getParcelableArrayListExtra(EXTRA_DATA)!!
            if(novelList3.size == 0) {
                binding?.noData?.visibility = View.VISIBLE
            } else {
                initRecyclerView()
            }
        } else if (option == "4") {
            novelList4 = intent.getParcelableArrayListExtra(EXTRA_DATA)!!
            if(novelList4.size == 0) {
                binding?.noData?.visibility = View.VISIBLE
            } else {
                initRecyclerView()
            }
        } else if (option == "5") {
            novelList5 = intent.getParcelableArrayListExtra(EXTRA_DATA)!!
            if(novelList5.size == 0) {
                binding?.noData?.visibility = View.VISIBLE
            } else {
                initRecyclerView()
            }
        }


        binding?.backButton?.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initRecyclerView() {
        binding?.rvNovel?.layoutManager = LinearLayoutManager(this)
        adapter = NovelAdapter(novelList1, novelList2, novelList3, novelList4, novelList5, option!!)
        binding?.rvNovel?.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        const val EXTRA_DATA ="data"
        const val OPTION = "option"
        const val TITLE = "title"
    }
}