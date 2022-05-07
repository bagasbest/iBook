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
import com.project.ibook.books.other.*
import com.project.ibook.databinding.ActivityHomepageBinding
import com.project.ibook.search.SearchActivity

class HomepageActivity : AppCompatActivity() {

    private var binding: ActivityHomepageBinding? = null
    private var adapter: NovelAdapter? = null
    private var novelList1 = ArrayList<NovelModel1>()
    private var novelList2 = ArrayList<NovelModel2>()
    private var novelList3 = ArrayList<NovelModel3>()
    private var novelList4 = ArrayList<NovelModel4>()
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

        binding?.next2?.setOnClickListener {
            val intent = Intent(this, NovelListActivity::class.java)
            intent.putExtra(NovelListActivity.EXTRA_DATA, novelList2)
            intent.putExtra(NovelListActivity.OPTION, "2")
            intent.putExtra(NovelListActivity.TITLE, "Pilihan iBook")
            startActivity(intent)
        }

        binding?.next5?.setOnClickListener {
            val intent = Intent(this, NovelListActivity::class.java)
            intent.putExtra(NovelListActivity.EXTRA_DATA, novelList5)
            intent.putExtra(NovelListActivity.OPTION, "5")
            intent.putExtra(NovelListActivity.TITLE, "Anda Mungkin Suka")
            startActivity(intent)
        }

    }

    private fun initView() {

        initRecyclerView2()
        initViewModel2()

        initRecyclerView5()
        initViewModel5()
    }

    private fun initRecyclerView2() {
        Handler().postDelayed({
            if (novelList2.size > 0) {
                binding?.rv2?.layoutManager = LinearLayoutManager(this)
                adapter = NovelAdapter(
                    novelList1,
                    novelList2,
                    novelList3,
                    novelList4,
                    novelList5,
                    "2"
                )
                binding?.rv2?.adapter = adapter
            }
        }, 2000)
    }

    private fun initViewModel2() {
        val viewModel = ViewModelProvider(this)[NovelViewModel2::class.java]
        binding?.progressBar2?.visibility = View.VISIBLE
        viewModel.setListBookByiBookChoiceLimited()
        viewModel.getBook().observe(this) { novelList ->
            if (novelList.size > 0) {
                novelList2.clear()
                binding?.noData2?.visibility = View.GONE
                novelList2.addAll(novelList)
            } else {
                binding?.noData2?.visibility = View.VISIBLE
            }
            binding!!.progressBar2.visibility = View.GONE
        }
    }

    private fun initRecyclerView5() {
        Handler().postDelayed({
           if(novelList5.size > 0) {
               binding?.rv5?.layoutManager = LinearLayoutManager(this)
               adapter = NovelAdapter(
                   novelList1,
                   novelList2,
                   novelList3,
                   novelList4,
                   novelList5,
                   "5"
               )
               binding?.rv5?.adapter = adapter
           }
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