package com.project.ibook.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.ibook.databinding.ActivitySearchBinding
import java.util.*

class SearchActivity : AppCompatActivity() {

    private var binding: ActivitySearchBinding? = null
    private var adapter: SearchAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        initRecyclerView()
        initViewModel("all")

        binding?.cancelBtn?.setOnClickListener {
            onBackPressed()
        }

        binding?.searchEt?.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(query: Editable?) {
                if(query.toString().isNotEmpty()) {
                    binding?.rvBook?.visibility = View.VISIBLE
                    binding?.noData?.visibility = View.GONE
                    val executedQuery = query.toString().lowercase(Locale.getDefault())
                    initRecyclerView()
                    initViewModel(executedQuery)
                } else {
                    initRecyclerView()
                    initViewModel("all")
                }
            }
        })
    }

    private fun initRecyclerView() {
        binding?.rvBook?.layoutManager = LinearLayoutManager(this)
        adapter = SearchAdapter()
        binding?.rvBook?.adapter = adapter
    }

    private fun initViewModel(executedQuery: String) {
        val viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        binding?.progressBar?.visibility = View.VISIBLE
        if(executedQuery == "all") {
            viewModel.setNovel()
        } else {
            viewModel.setNovelByQuery(executedQuery)
        }
        viewModel.getBook().observe(this) { bookList ->
            if (bookList.size > 0) {
                binding?.noData?.visibility = View.GONE
                adapter?.setData(bookList)
            } else {
                binding?.noData?.visibility = View.VISIBLE
            }
            binding!!.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}