package com.project.ibook.books.write

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.ibook.HomepageActivity
import com.project.ibook.R
import com.project.ibook.books.my_book.*
import com.project.ibook.databinding.ActivityWriteNovelBinding

class WriteNovelActivity : AppCompatActivity() {

    private var binding: ActivityWriteNovelBinding? = null
    private var model: MyBookModel? = null
    private var adapter: MyBookBabAdapter? = null


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteNovelBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        model = intent.getParcelableExtra(EXTRA_DATA)
        initRecyclerView()
        Glide.with(this)
            .load(model?.image)
            .into(binding!!.image)

        binding?.textView4?.text = model?.title
        binding?.genre?.text = "Genre: ${model?.genre}"
        binding?.synopsis?.text = model?.synopsis
        binding?.viewTime?.text = "${model?.viewTime}\nKali Dilihat"
        getWordCountNovel(model?.babList!!)

        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        if(model?.writerUid == uid) {
            binding?.addNewBab?.visibility = View.VISIBLE
            binding?.edit?.visibility = View.VISIBLE
            binding?.delete?.visibility = View.VISIBLE
        }

        binding?.backButton?.setOnClickListener {
            onBackPressed()
        }

        binding?.addNewBab?.setOnClickListener {
            val intent = Intent(this, MyBookBabAddActivity::class.java)
            intent.putExtra(MyBookBabAddActivity.BAB_LIST, model?.babList)
            intent.putExtra(MyBookBabAddActivity.NOVEL_ID, model?.uid)
            startActivity(intent)
        }

        binding?.edit?.setOnClickListener {
            val intent = Intent(this, MyBookEditActivity::class.java)
            intent.putExtra(MyBookEditActivity.EXTRA_DATA, model)
            startActivity(intent)
        }

        binding?.delete?.setOnClickListener {
            showConfirmDeleteDialog()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getWordCountNovel(babList: ArrayList<MyBookBabModel>) {
        var wordCount = 0L
        for(i in babList.indices) {
            val description = babList[i].description.toString().trim()
            wordCount += description.split("\\s+".toRegex()).size
        }
        binding?.wordCount?.text = "$wordCount\nKata"
    }

    private fun showConfirmDeleteDialog() {
        AlertDialog.Builder(this)
            .setTitle("Konfimasi Menghapus Novel")
            .setMessage("Apakah anda yakin ingin menghapus novel ini ?")
            .setIcon(R.drawable.ic_baseline_warning_24)
            .setPositiveButton("YA") { dialogInterface, _ ->
                dialogInterface.dismiss()
                deleteNovel()
            }
            .setNegativeButton("TIDAK", null)
            .show()
    }

    private fun deleteNovel() {
        FirebaseFirestore
            .getInstance()
            .collection("novel")
            .document(model?.uid!!)
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
    private fun initRecyclerView() {
        if(model?.babList == null) {
            binding?.noData?.visibility = View.VISIBLE
        } else {
            binding?.rvBab?.layoutManager = LinearLayoutManager(this)
            binding?.bab?.text = "${model?.babList?.size}\nBab"
            adapter = MyBookBabAdapter(model?.babList!!, model?.uid, model?.writerUid)
            binding?.rvBab?.adapter = adapter
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        const val EXTRA_DATA = "data"
    }
}