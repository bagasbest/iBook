package com.project.ibook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.project.ibook.books.my_book.MyBookActivity
import com.project.ibook.books.other.NovelAdapter
import com.project.ibook.books.other.NovelModel5
import com.project.ibook.books.other.NovelViewModel5
import com.project.ibook.databinding.ActivityHomepageBinding
import com.project.ibook.search.SearchActivity

class HomepageActivity : AppCompatActivity() {

    private var binding: ActivityHomepageBinding? = null
    private var adapter: NovelAdapter? = null
    private var novelList5 = ArrayList<NovelModel5>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        initView()

        binding?.logoutBtn?.setOnClickListener {
            logout()
        }

        binding?.searchBtn?.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        binding?.write?.setOnClickListener {
            startActivity(Intent(this, MyBookActivity::class.java))
        }

    }

    private fun initView() {
        initRecyclerView5()
        initViewModel5()
    }

    private fun initRecyclerView5() {
        Handler().postDelayed({
            binding?.rv5?.layoutManager = LinearLayoutManager(this)
            adapter = NovelAdapter(novelList5, "5")
            binding?.rv5?.adapter = adapter
        }, 2000)
    }

    private fun initViewModel5() {
        val viewModel = ViewModelProvider(this)[NovelViewModel5::class.java]
        binding?.progressBar5?.visibility = View.VISIBLE
        viewModel.setListBookLimited()
        viewModel.getBook().observe(this) { novelList ->
            if (novelList.size > 0) {
                novelList5.clear()
                binding?.noData5?.visibility = View.GONE
                novelList5.addAll(novelList)
                Log.e("tad", novelList5.size.toString())
            } else {
                binding?.noData5?.visibility = View.VISIBLE
            }
            binding!!.progressBar5.visibility = View.GONE
        }
    }

    private fun logout() {
        AlertDialog.Builder(this)
            .setTitle("Konfirmasi Logout")
            .setMessage("Apakah anda yakin ingin keluar aplikasi ?")
            .setIcon(R.drawable.ic_baseline_warning_24)
            .setPositiveButton("YA") { dialogInterface, _ ->
                dialogInterface.dismiss()
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            .setNegativeButton("TIDAK", null)
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}