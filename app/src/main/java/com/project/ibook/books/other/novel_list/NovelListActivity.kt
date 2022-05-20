package com.project.ibook.books.other.novel_list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.ibook.books.other.anda_mungkin_suka.NovelModel5
import com.project.ibook.books.other.anda_mungkin_suka.NovelViewModel5
import com.project.ibook.books.other.cinta_abadi.NovelModel4
import com.project.ibook.books.other.cinta_abadi.NovelViewModel4
import com.project.ibook.books.other.pilihan_iBook.NovelModel2
import com.project.ibook.books.other.pilihan_iBook.NovelViewModel2
import com.project.ibook.books.other.terlaris.NovelModel1
import com.project.ibook.books.other.terlaris.NovelViewModel1
import com.project.ibook.books.other.wajib_dibaca.NovelModel3
import com.project.ibook.books.other.wajib_dibaca.NovelViewModel3
import com.project.ibook.databinding.ActivityNovelListBinding

class NovelListActivity : AppCompatActivity() {

    private var binding: ActivityNovelListBinding? = null
    private var novelList1 = ArrayList<NovelModel1>()
    private var novelList2 = ArrayList<NovelModel2>()
    private var novelList3 = ArrayList<NovelModel3>()
    private var novelList4 = ArrayList<NovelModel4>()
    private var novelList5 = ArrayList<NovelModel5>()
    private var adapter: NovelListAdapter? = null
    private var option: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNovelListBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        option = intent.getStringExtra(OPTION)
        binding?.textView3?.text = intent.getStringExtra(TITLE)
        when (option) {
            "1" -> {
                initRecyclerView()
                initViewModel1()
            }
            "2" -> {
                initRecyclerView()
                initViewModel2()
            }
            "3" -> {
                initRecyclerView()
                initViewModel3()
            }
            "4" -> {
                initRecyclerView()
                initViewModel4()
            }
            "5" -> {
                initRecyclerView()
                initViewModel5()
            }
        }

        binding?.backButton?.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initRecyclerView() {
        Handler().postDelayed({
            novelList1.sortByDescending { it.coins }
            novelList2.sortByDescending { it.coins }
            novelList3.sortByDescending { it.coins }
            novelList4.sortByDescending { it.coins }
            novelList5.sortByDescending { it.coins }
            binding?.rvNovel?.layoutManager = LinearLayoutManager(this)
            adapter =
                NovelListAdapter(novelList1, novelList2, novelList3, novelList4, novelList5, option!!)
            binding?.rvNovel?.adapter = adapter
        }, 1000)
    }

    private fun initViewModel1() {
        val viewModel = ViewModelProvider(this)[NovelViewModel1::class.java]
        binding?.progressBar?.visibility = View.VISIBLE
        viewModel.setListBookByFamousAll()
        viewModel.getBook().observe(this) { novelList ->
            if (novelList.size > 0) {
                novelList1.clear()
                binding?.noData?.visibility = View.GONE
                novelList1.addAll(novelList)
            } else {
                binding?.noData?.visibility = View.VISIBLE
            }
            binding?.progressBar?.visibility = View.GONE
        }
    }

    private fun initViewModel2() {
        val viewModel = ViewModelProvider(this)[NovelViewModel2::class.java]
        binding?.progressBar?.visibility = View.VISIBLE
        viewModel.setListBookByiBookChoiceAll()
        viewModel.getBook().observe(this) { novelList ->
            if (novelList.size > 0) {
                novelList2.clear()
                binding?.noData?.visibility = View.GONE
                novelList2.addAll(novelList)
            } else {
                binding?.noData?.visibility = View.VISIBLE
            }
            binding?.progressBar?.visibility = View.GONE
        }
    }

    private fun initViewModel3() {
        val viewModel = ViewModelProvider(this)[NovelViewModel3::class.java]
        binding?.progressBar?.visibility = View.VISIBLE
        viewModel.setListBookByMustReadAll()
        viewModel.getBook().observe(this) { novelList ->
            if (novelList.size > 0) {
                novelList3.clear()
                binding?.noData?.visibility = View.GONE
                novelList3.addAll(novelList)
            } else {
                binding?.noData?.visibility = View.VISIBLE
            }
            binding?.progressBar?.visibility = View.GONE
        }
    }


    private fun initViewModel4() {
        val viewModel = ViewModelProvider(this)[NovelViewModel4::class.java]
        binding?.progressBar?.visibility = View.VISIBLE
        viewModel.setListBookByLoveForeverAll()
        viewModel.getBook().observe(this) { novelList ->
            if (novelList.size > 0) {
                novelList4.clear()
                binding?.noData?.visibility = View.GONE
                novelList4.addAll(novelList)
            } else {
                binding?.noData?.visibility = View.VISIBLE
            }
            binding?.progressBar?.visibility = View.GONE
        }
    }

    private fun initViewModel5() {
        val viewModel = ViewModelProvider(this)[NovelViewModel5::class.java]
        binding?.progressBar?.visibility = View.VISIBLE
        viewModel.setListBookAll()
        viewModel.getBook().observe(this) { novelList ->
            if (novelList.size > 0) {
                novelList5.clear()
                binding?.noData?.visibility = View.GONE
                novelList5.addAll(novelList)
            } else {
                binding?.noData?.visibility = View.VISIBLE
            }
            binding?.progressBar?.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        const val OPTION = "option"
        const val TITLE = "title"
    }
}