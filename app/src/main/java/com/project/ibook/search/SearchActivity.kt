package com.project.ibook.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.project.ibook.databinding.ActivitySearchBinding
import java.util.*

class SearchActivity : AppCompatActivity() {

    private var binding: ActivitySearchBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding?.root)

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
                    binding?.noData?.visibility = View.GONE
                    val executedQuery = query.toString().lowercase(Locale.getDefault())
                } else {
                    binding?.noData?.visibility = View.VISIBLE
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}