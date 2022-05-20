package com.project.ibook.ui.book

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.ibook.MainActivity
import com.project.ibook.books.other.terlaris.NovelModel1
import com.project.ibook.books.other.terlaris.NovelViewModel1
import com.project.ibook.databinding.FragmentBookBinding

class BookFragment : Fragment() {

    private var _binding: FragmentBookBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var adapter: BookAdapter? = null
    private var novelList = ArrayList<NovelModel1>()
    private var novelUidList = ArrayList<NovelModel1>()

    override fun onResume() {
        super.onResume()
        novelList.clear()
        novelUidList.clear()
        checkIsLoginOrNot()
    }

    private fun checkIsLoginOrNot() {
        if(FirebaseAuth.getInstance().currentUser != null) {
            binding.noLogin.visibility = View.GONE
            binding.content.visibility = View.VISIBLE

            initViewModel()

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentBookBinding.inflate(inflater, container, false)

        return binding.root
    }

    private fun initRecyclerView() {

        binding.rvBook.layoutManager = StaggeredGridLayoutManager(
            3,
            StaggeredGridLayoutManager.VERTICAL
        )
        adapter = BookAdapter(novelList)
        binding.rvBook.adapter = adapter
        binding.progressBar.visibility = View.GONE

    }

    private fun initViewModel() {
        val viewModel = ViewModelProvider(this)[NovelViewModel1::class.java]

        binding.progressBar.visibility = View.VISIBLE
        val uid = FirebaseAuth.getInstance().currentUser!!.uid

        viewModel.setListMyBookRack(uid)
        viewModel.getBook().observe(viewLifecycleOwner) { bookUidList ->
            if (bookUidList.size > 0) {
                novelUidList.clear()
                binding.noData.visibility = View.GONE
                novelUidList.addAll(bookUidList)
            } else {
                novelUidList.clear()
                binding.progressBar.visibility = View.GONE
                binding.noData.visibility = View.VISIBLE
            }
        }

        Handler().postDelayed({
            for(i in novelUidList.indices) {
                FirebaseFirestore.getInstance()
                    .collection("novel")
                    .document(novelUidList[i].uid!!)
                    .get()
                    .addOnSuccessListener { document ->

                        if(document.exists()) {
                            val model = NovelModel1()

                            model.title = document.data!!["title"].toString()
                            model.uid = document.data!!["uid"].toString()
                            model.synopsis = document.data!!["synopsis"].toString()
                            model.status = document.data!!["status"].toString()
                            model.writerName = document.data!!["writerName"].toString()
                            model.writerUid = document.data!!["writerUid"].toString()
                            model.genre = document.data!!["genre"] as ArrayList<String>
                            model.image = document.data!!["image"].toString()
                            model.viewTime = document.data!!["viewTime"] as Long
                            model.babList = document.toObject(NovelModel1::class.java)?.babList


                            novelList.add(model)
                        }
                    }
            }


            Handler().postDelayed({
                initRecyclerView()
            }, 500)
        },500)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginBtn.setOnClickListener {
            startActivity(Intent(activity, MainActivity::class.java))
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}