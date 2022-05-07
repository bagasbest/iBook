package com.project.ibook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.project.ibook.books.my_book.MyBookActivity
import com.project.ibook.books.other.*
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

        binding?.next1?.setOnClickListener {
            val intent = Intent(this, NovelListActivity::class.java)
            intent.putExtra(NovelListActivity.EXTRA_DATA, novelList2)
            intent.putExtra(NovelListActivity.OPTION, "2")
            intent.putExtra(NovelListActivity.TITLE, "Pilihan iBook")
            startActivity(intent)
        }

        binding?.next2?.setOnClickListener {
            val intent = Intent(this, NovelListActivity::class.java)
            intent.putExtra(NovelListActivity.EXTRA_DATA, novelList2)
            intent.putExtra(NovelListActivity.OPTION, "2")
            intent.putExtra(NovelListActivity.TITLE, "Pilihan iBook")
            startActivity(intent)
        }

        binding?.next3?.setOnClickListener {
            val intent = Intent(this, NovelListActivity::class.java)
            intent.putExtra(NovelListActivity.EXTRA_DATA, novelList3)
            intent.putExtra(NovelListActivity.OPTION, "3")
            intent.putExtra(NovelListActivity.TITLE, "Wajib Dibaca")
            startActivity(intent)
        }

        binding?.next4?.setOnClickListener {
            val intent = Intent(this, NovelListActivity::class.java)
            intent.putExtra(NovelListActivity.EXTRA_DATA, novelList4)
            intent.putExtra(NovelListActivity.OPTION, "4")
            intent.putExtra(NovelListActivity.TITLE, "Cinta Abadi")
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

        initRecyclerView1()
        initViewModel1()

        initRecyclerView2()
        initViewModel2()

        initRecyclerView3()
        initViewModel3()

        initRecyclerView4()
        initViewModel4()

        initRecyclerView5()
        initViewModel5()
    }

    private fun initRecyclerView1() {
        Handler().postDelayed({
            if (novelList1.size > 0) {
                binding?.rv1?.layoutManager = StaggeredGridLayoutManager(
                    2,
                    StaggeredGridLayoutManager.HORIZONTAL
                )
                adapter = NovelAdapter(
                    novelList1,
                    novelList2,
                    novelList3,
                    novelList4,
                    novelList5,
                    "1"
                )
                binding?.rv1?.adapter = adapter
            }
        }, 2000)
    }

    private fun initViewModel1() {
        val viewModel = ViewModelProvider(this)[NovelViewModel1::class.java]
        binding?.progressBar1?.visibility = View.VISIBLE
        viewModel.setListBookByFamousLimited()
        viewModel.getBook().observe(this) { novelList ->
            if (novelList.size > 0) {
                novelList1.clear()
                binding?.noData1?.visibility = View.GONE
                novelList1.addAll(novelList)
            } else {
                binding?.noData1?.visibility = View.VISIBLE
            }
            binding!!.progressBar1.visibility = View.GONE
        }
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

    private fun initRecyclerView3() {
        Handler().postDelayed({
            if(novelList3.size > 0) {
                binding?.rv3?.layoutManager = LinearLayoutManager(this)
                adapter = NovelAdapter(
                    novelList1,
                    novelList2,
                    novelList3,
                    novelList4,
                    novelList5,
                    "3"
                )
                binding?.rv3?.adapter = adapter
            }
        }, 2000)
    }

    private fun initViewModel3() {
        val viewModel = ViewModelProvider(this)[NovelViewModel3::class.java]
        binding?.progressBar3?.visibility = View.VISIBLE
        viewModel.setListBookByMustReadLimited()
        viewModel.getBook().observe(this) { novelList ->
            if (novelList.size > 0) {
                novelList3.clear()
                binding?.noData3?.visibility = View.GONE
                novelList3.addAll(novelList)
            } else {
                binding?.noData3?.visibility = View.VISIBLE
            }
            binding!!.progressBar3.visibility = View.GONE
        }
    }

    private fun initRecyclerView4() {
        Handler().postDelayed({
            if(novelList4.size > 0) {
                binding?.rv4?.layoutManager = LinearLayoutManager(this)
                adapter = NovelAdapter(
                    novelList1,
                    novelList2,
                    novelList3,
                    novelList4,
                    novelList5,
                    "4"
                )
                binding?.rv4?.adapter = adapter
            }
        }, 2000)
    }

    private fun initViewModel4() {
        val viewModel = ViewModelProvider(this)[NovelViewModel4::class.java]
        binding?.progressBar4?.visibility = View.VISIBLE
        viewModel.setListBookByLoveForeverLimited()
        viewModel.getBook().observe(this) { novelList ->
            if (novelList.size > 0) {
                novelList4.clear()
                binding?.noData4?.visibility = View.GONE
                novelList4.addAll(novelList)
            } else {
                binding?.noData4?.visibility = View.VISIBLE
            }
            binding!!.progressBar4.visibility = View.GONE
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