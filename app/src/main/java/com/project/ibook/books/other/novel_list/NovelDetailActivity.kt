package com.project.ibook.books.other.novel_list

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.ibook.HomepageActivity
import com.project.ibook.R
import com.project.ibook.books.my_book.add_edit_bab_novel.MyBookBabModel
import com.project.ibook.books.other.NovelReadAdapter
import com.project.ibook.books.other.anda_mungkin_suka.NovelModel5
import com.project.ibook.books.other.cinta_abadi.NovelModel4
import com.project.ibook.books.other.pilihan_iBook.NovelModel2
import com.project.ibook.books.other.terlaris.NovelModel1
import com.project.ibook.books.other.wajib_dibaca.NovelModel3
import com.project.ibook.databinding.ActivityNovelDetailBinding
import java.text.DecimalFormat

class NovelDetailActivity : AppCompatActivity() {

    private var binding: ActivityNovelDetailBinding? = null
    private var model1: NovelModel1? = null
    private var model2: NovelModel2? = null
    private var model3: NovelModel3? = null
    private var model4: NovelModel4? = null
    private var model5: NovelModel5? = null
    private var adapter: NovelReadAdapter? = null
    private var homepageCategory: String? = null
    private val formatter = DecimalFormat("#,###")

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNovelDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        checkRole()
        showDropdownHomepageCategory()
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        when (intent.getStringExtra(OPTION)) {
            "1" -> {
                model1 = intent.getParcelableExtra(EXTRA_DATA)
                initRecyclerView1()
                Glide.with(this)
                    .load(model1?.image)
                    .into(binding!!.image)

                binding?.textView4?.text = model1?.title
                binding?.genre?.text = "Genre: ${model1?.genre}"
                binding?.synopsis?.text = model1?.synopsis
                binding?.viewTime?.text = "${model1?.viewTime}\nKali Dilihat"
                getWordCountNovel(model1?.babList!!)
                if(uid != model1?.writerUid) {
                    getTotalView(model1?.viewTime!!, model1?.uid!!)
                } else {
                    binding?.viewTime?.text = "${formatter.format(model1?.viewTime)}\nKali Dilihat"
                }
            }
            "2" -> {
                model2 = intent.getParcelableExtra(EXTRA_DATA)
                initRecyclerView2()
                Glide.with(this)
                    .load(model2?.image)
                    .into(binding!!.image)

                binding?.textView4?.text = model2?.title
                binding?.genre?.text = "Genre: ${model2?.genre}"
                binding?.synopsis?.text = model2?.synopsis
                binding?.viewTime?.text = "${model2?.viewTime}\nKali Dilihat"
                getWordCountNovel(model2?.babList!!)
                if(uid != model2?.writerUid) {
                    getTotalView(model2?.viewTime!!, model2?.uid!!)
                } else {
                    binding?.viewTime?.text = "${formatter.format(model2?.viewTime)}\nKali Dilihat"
                }
            }
            "3" -> {
                model3 = intent.getParcelableExtra(EXTRA_DATA)
                initRecyclerView3()
                Glide.with(this)
                    .load(model3?.image)
                    .into(binding!!.image)

                binding?.textView4?.text = model3?.title
                binding?.genre?.text = "Genre: ${model3?.genre}"
                binding?.synopsis?.text = model3?.synopsis
                binding?.viewTime?.text = "${model3?.viewTime}\nKali Dilihat"
                getWordCountNovel(model3?.babList!!)
                if(uid != model3?.writerUid) {
                    getTotalView(model3?.viewTime!!, model3?.uid!!)
                } else {
                    binding?.viewTime?.text = "${formatter.format(model3?.viewTime)}\nKali Dilihat"
                }
            }
            "4" -> {
                model4 = intent.getParcelableExtra(EXTRA_DATA)
                initRecyclerView4()
                Glide.with(this)
                    .load(model4?.image)
                    .into(binding!!.image)

                binding?.textView4?.text = model4?.title
                binding?.genre?.text = "Genre: ${model4?.genre}"
                binding?.synopsis?.text = model4?.synopsis
                binding?.viewTime?.text = "${model4?.viewTime}\nKali Dilihat"
                getWordCountNovel(model4?.babList!!)
                if(uid != model4?.writerUid) {
                    getTotalView(model4?.viewTime!!, model4?.uid!!)
                } else {
                    binding?.viewTime?.text = "${formatter.format(model4?.viewTime)}\nKali Dilihat"
                }
            }
            else -> {
                model5 = intent.getParcelableExtra(EXTRA_DATA)
                initRecyclerView5()
                Glide.with(this)
                    .load(model5?.image)
                    .into(binding!!.image)

                binding?.textView4?.text = model5?.title
                binding?.genre?.text = "Genre: ${model5?.genre}"
                binding?.synopsis?.text = model5?.synopsis
                binding?.viewTime?.text = "${model5?.viewTime}\nKali Dilihat"
                getWordCountNovel(model5?.babList!!)

                Log.e("tag", uid)
                Log.e("tag",model5?.writerUid!!)

                if(uid != model5?.writerUid) {
                    getTotalView(model5?.viewTime!!, model5?.uid!!)
                } else {
                    binding?.viewTime?.text = "${formatter.format(model5?.viewTime)}\nKali Dilihat"
                }
            }
        }

        binding?.backButton?.setOnClickListener {
            onBackPressed()
        }

        binding?.saveCategory?.setOnClickListener {
            when (intent.getStringExtra(OPTION)) {
                "1" -> {
                    formValidation(model1?.uid!!)

                }
                "2" -> {
                    formValidation(model2?.uid!!)

                }
                "3" -> {
                    formValidation(model3?.uid!!)

                }
                "4" -> {
                    formValidation(model4?.uid!!)

                }
                else -> {
                    formValidation(model5?.uid!!)

                }
            }
        }

        binding?.deleteCategory?.setOnClickListener {
            FirebaseFirestore
                .getInstance()
                .collection("novel")
                .document(model5?.uid!!)
                .update("homepageCategory", "")
                .addOnCompleteListener {
                    if(it.isSuccessful) {
                        Toast.makeText(this, "Novel ini tidak akan tampil di halaman utama", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Ups, operasi gagal, silahkan coba lagi nanti", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        binding?.delete?.setOnClickListener {
            showConfirmDeleteDialog()
        }
    }

    private fun showConfirmDeleteDialog() {
        AlertDialog.Builder(this)
            .setTitle("Konfimasi Menghapus Novel")
            .setMessage("Apakah anda yakin ingin menghapus novel ini ?")
            .setIcon(R.drawable.ic_baseline_warning_24)
            .setPositiveButton("YA") { dialogInterface, _ ->
                dialogInterface.dismiss()
                when (intent.getStringExtra(OPTION)) {
                    "1" -> {
                        deleteNovel(model1?.uid!!)
                    }
                    "2" -> {
                        deleteNovel(model2?.uid!!)
                    }
                    "3" -> {
                        deleteNovel(model3?.uid!!)
                    }
                    "4" -> {
                        deleteNovel(model4?.uid!!)
                    }
                    else -> {
                        deleteNovel(model5?.uid!!)
                    }
                }
            }
            .setNegativeButton("TIDAK", null)
            .show()
    }

    private fun deleteNovel(uid: String) {
        FirebaseFirestore
            .getInstance()
            .collection("novel")
            .document(uid)
            .delete()
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    showSuccessDialog()
                } else {
                    showFailureDialog()
                }
            }
    }


    private fun showFailureDialog() {
        AlertDialog.Builder(this)
            .setTitle("Gagal Menghapus Novel")
            .setMessage("Ups, sepertinya koneksi internet anda tidak stabil, silahkan coba lagi nanti")
            .setIcon(R.drawable.ic_baseline_clear_24)
            .setPositiveButton("OKE") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .show()
    }

    private fun showSuccessDialog() {
        AlertDialog.Builder(this)
            .setTitle("Berhasil Menghapus Novel")
            .setMessage("Novel anda berhasil di hapus")
            .setIcon(R.drawable.ic_baseline_check_circle_outline_24)
            .setPositiveButton("OKE") { dialogInterface, _ ->
                dialogInterface.dismiss()
                val intent = Intent(this, HomepageActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            .show()
    }

    @SuppressLint("SetTextI18n")
    private fun getTotalView(viewTime: Long, uid: String) {
        binding?.viewTime?.text = "${formatter.format(viewTime+1)}\nKali Dilihat"
        FirebaseFirestore
            .getInstance()
            .collection("novel")
            .document(uid)
            .update("viewTime", viewTime+1)
    }

    private fun formValidation(uid: String) {
        if(homepageCategory == null) {
            Toast.makeText(this, "Anda harus memilih kategori novel terlebih dahulu", Toast.LENGTH_SHORT).show()
        } else {
            FirebaseFirestore
                .getInstance()
                .collection("novel")
                .document(uid)
                .update("homepageCategory", homepageCategory)
                .addOnCompleteListener {
                    if(it.isSuccessful) {
                        Toast.makeText(this, "Novel ini akan tampil di halaman utama, pada kategori $homepageCategory", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Ups, gagal menyimpan kategori novel, silahkan coba lagi nanti", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun showDropdownHomepageCategory() {
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.homepage_category, android.R.layout.simple_list_item_1
        )
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        binding?.homepageCategory?.setAdapter(adapter)
        binding?.homepageCategory?.setOnItemClickListener { _, _, _, _ ->
            homepageCategory = binding?.homepageCategory!!.text.toString()
        }
    }

    private fun checkRole() {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        FirebaseFirestore
            .getInstance()
            .collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener {
                if("" + it.data!!["role"] == "admin") {
                    binding?.adminSide?.visibility = View.VISIBLE
                    binding?.delete?.visibility = View.VISIBLE
                }
            }
    }


    @SuppressLint("SetTextI18n")
    private fun getWordCountNovel(babList: ArrayList<MyBookBabModel>) {
        val formatter = DecimalFormat("#,###")
        var wordCount = 0L
        for(i in babList.indices) {
            if(babList[i].status == "Published") {
                val description = babList[i].description.toString().trim()
                wordCount += description.split("\\s+".toRegex()).size
            }
        }

        binding?.wordCount?.text = "${formatter.format(wordCount)}\nKata"
    }

    @SuppressLint("SetTextI18n")
    private fun initRecyclerView1() {
        if(model1?.babList == null) {
            binding?.noData?.visibility = View.VISIBLE
        } else {
            val publishedBab = ArrayList<MyBookBabModel>()
            binding?.progressBar?.visibility = View.VISIBLE

            for(i in model1?.babList!!.indices) {
                if(model1?.babList!![i].status == "Published") {
                    publishedBab.add(model1?.babList!![i])
                }
            }

            Handler().postDelayed({
                binding?.progressBar?.visibility = View.GONE
                if(publishedBab.size > 0) {
                    binding?.rvBab?.layoutManager = LinearLayoutManager(this)
                    binding?.bab?.text = "${publishedBab.size}\nBab"
                    adapter = NovelReadAdapter(publishedBab)
                    binding?.rvBab?.adapter = adapter
                } else {
                    binding?.noData?.visibility = View.VISIBLE
                }
            }, 2000)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initRecyclerView2() {
        if(model2?.babList == null) {
            binding?.noData?.visibility = View.VISIBLE
        } else {
            val publishedBab = ArrayList<MyBookBabModel>()
            binding?.progressBar?.visibility = View.VISIBLE

            for(i in model2?.babList!!.indices) {
                if(model2?.babList!![i].status == "Published") {
                    publishedBab.add(model2?.babList!![i])
                }
            }

            Handler().postDelayed({
                binding?.progressBar?.visibility = View.GONE
                if(publishedBab.size > 0) {
                    binding?.rvBab?.layoutManager = LinearLayoutManager(this)
                    binding?.bab?.text = "${publishedBab.size}\nBab"
                    adapter = NovelReadAdapter(publishedBab)
                    binding?.rvBab?.adapter = adapter
                } else {
                    binding?.noData?.visibility = View.VISIBLE
                }
            }, 2000)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initRecyclerView3() {
        if(model3?.babList == null) {
            binding?.noData?.visibility = View.VISIBLE
        } else {
            val publishedBab = ArrayList<MyBookBabModel>()
            binding?.progressBar?.visibility = View.VISIBLE

            for(i in model3?.babList!!.indices) {
                if(model3?.babList!![i].status == "Published") {
                    publishedBab.add(model3?.babList!![i])
                }
            }

            Handler().postDelayed({
                binding?.progressBar?.visibility = View.GONE
                if(publishedBab.size > 0) {
                    binding?.rvBab?.layoutManager = LinearLayoutManager(this)
                    binding?.bab?.text = "${publishedBab.size}\nBab"
                    adapter = NovelReadAdapter(publishedBab)
                    binding?.rvBab?.adapter = adapter
                }else {
                    binding?.noData?.visibility = View.VISIBLE
                }

            }, 2000)
        }
    }


    @SuppressLint("SetTextI18n")
    private fun initRecyclerView4() {
        if(model4?.babList == null) {
            binding?.noData?.visibility = View.VISIBLE
        } else {
            val publishedBab = ArrayList<MyBookBabModel>()
            binding?.progressBar?.visibility = View.VISIBLE

            for(i in model4?.babList!!.indices) {
                if(model4?.babList!![i].status == "Published") {
                    publishedBab.add(model4?.babList!![i])
                }
            }

            Handler().postDelayed({
                binding?.progressBar?.visibility = View.GONE
                if(publishedBab.size > 0) {
                    binding?.rvBab?.layoutManager = LinearLayoutManager(this)
                    binding?.bab?.text = "${publishedBab.size}\nBab"
                    adapter = NovelReadAdapter(publishedBab)
                    binding?.rvBab?.adapter = adapter
                } else {
                    binding?.noData?.visibility = View.VISIBLE
                }
            }, 2000)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initRecyclerView5() {
        if(model5?.babList == null) {
            binding?.noData?.visibility = View.VISIBLE
        } else {
            val publishedBab = ArrayList<MyBookBabModel>()
            binding?.progressBar?.visibility = View.VISIBLE

            for(i in model5?.babList!!.indices) {
                if(model5?.babList!![i].status == "Published") {
                    publishedBab.add(model5?.babList!![i])
                }
            }

            Handler().postDelayed({
                binding?.progressBar?.visibility = View.GONE
                if(publishedBab.size > 0) {
                    binding?.rvBab?.layoutManager = LinearLayoutManager(this)
                    binding?.bab?.text = "${publishedBab.size}\nBab"
                    adapter = NovelReadAdapter(publishedBab)
                    binding?.rvBab?.adapter = adapter
                } else {
                    binding?.noData?.visibility = View.VISIBLE
                }

            }, 2000)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        const val EXTRA_DATA = "data"
        const val OPTION = "option"
    }
}