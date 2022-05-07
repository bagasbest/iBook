package com.project.ibook.books.my_book.add_edit_bab_novel

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.ibook.HomepageActivity
import com.project.ibook.R
import com.project.ibook.databinding.ActivityMyBookBabDetailBinding

class MyBookBabDetailActivity : AppCompatActivity() {

    private var binding: ActivityMyBookBabDetailBinding? = null
    private var myBookBabModel: MyBookBabModel? = null
    private var babList: ArrayList<MyBookBabModel>? = null
    private var newBabList = ArrayList<MyBookBabModel>()
    private var babStatus: String? = null

    private var isReadMode = true

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyBookBabDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        myBookBabModel = intent.getParcelableExtra(EXTRA_DATA)
        babList = intent.getParcelableArrayListExtra(BAB_LIST)
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        binding?.textView3?.text = myBookBabModel?.title
        if (intent.getStringExtra(WRITER_ID) == uid) {
            binding?.pratinjau?.visibility = View.VISIBLE
            binding?.description?.visibility = View.VISIBLE
            binding?.description?.text = myBookBabModel?.description
            binding?.title?.setText(myBookBabModel?.title)
            binding?.sinopsis?.setText(myBookBabModel?.description)
            babStatus = myBookBabModel?.status
        } else {
            binding?.description?.visibility = View.VISIBLE
            binding?.description?.text = myBookBabModel?.description
        }

        showDropdownBabStatus()

        binding?.saveDraft?.setOnClickListener {
            formValidation()
        }

        binding?.pratinjau?.setOnClickListener {
            if (!isReadMode) {
                binding?.writerSide?.visibility = View.GONE
                binding?.description?.visibility = View.VISIBLE
                isReadMode = true
                binding?.pratinjau?.text = "Editor Mode"
            } else {
                binding?.writerSide?.visibility = View.VISIBLE
                binding?.description?.visibility = View.GONE
                isReadMode = false
                binding?.pratinjau?.text = "Pratinjau Bab"
            }
        }

        binding?.backButton?.setOnClickListener {
            val intent = Intent(this, HomepageActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        binding?.delete?.setOnClickListener {
            showConfirmDeleteDialog()
        }
    }

    private fun showDropdownBabStatus() {
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.bab_status, android.R.layout.simple_list_item_1
        )
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        binding?.babStatus?.setAdapter(adapter)
        binding?.babStatus?.setOnItemClickListener { _, _, _, _ ->
            val status = binding?.babStatus!!.text.toString()
            babStatus = if(status == "Draft (Bab belum di publikasikan)") {
                "Draft"
            } else {
                "Published"
            }
        }
    }

    private fun showConfirmDeleteDialog() {
        AlertDialog.Builder(this)
            .setTitle("Konfimasi Menghapus Bab Ini")
            .setMessage("Apakah anda yakin ingin menghapus bab ini ?")
            .setIcon(R.drawable.ic_baseline_warning_24)
            .setPositiveButton("YA") { dialogInterface, _ ->
                dialogInterface.dismiss()
                deleteNovel()
            }
            .setNegativeButton("TIDAK", null)
            .show()
    }

    private fun deleteNovel() {
        val babNo = intent.getIntExtra(BAB_NO, 0)
        babList?.removeAt(babNo)

        FirebaseFirestore
            .getInstance()
            .collection("novel")
            .document(intent.getStringExtra(NOVEL_ID)!!)
            .update("babList", babList)
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
            .setTitle("Gagal Menghapus Bab Ini")
            .setMessage("Ups, sepertinya koneksi internet anda tidak stabil, silahkan coba lagi nanti")
            .setIcon(R.drawable.ic_baseline_clear_24)
            .setPositiveButton("OKE") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .show()
    }

    private fun showSuccessDialog() {
        AlertDialog.Builder(this)
            .setTitle("Berhasil Menghapus Bab Ini")
            .setMessage("Bab Ini berhasil di hapus")
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

    private fun formValidation() {
        val title = binding?.title?.text.toString().trim()
        val description = binding?.sinopsis?.text.toString().trim()

        when {
            title.isEmpty() -> {
                Toast.makeText(this, "Judul bab tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            }
            description.isEmpty() -> {
                Toast.makeText(this, "Isi bab tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            }
            else -> {
                val mProgressDialog = ProgressDialog(this)
                mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...")
                mProgressDialog.setCanceledOnTouchOutside(false)
                mProgressDialog.show()
                newBabList.clear()

                val uid = System.currentTimeMillis().toString()
                val model = MyBookBabModel(
                    uid = uid,
                    title = title,
                    description = description,
                    status = babStatus,
                )

                newBabList.addAll(babList!!)
                newBabList[babList!!.size-1] = model

                Log.e("tag", babList?.size.toString())
                Log.e("tag", newBabList.size.toString())


                FirebaseFirestore
                    .getInstance()
                    .collection("novel")
                    .document(intent.getStringExtra(NOVEL_ID)!!)
                    .update("babList", newBabList)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            mProgressDialog.dismiss()
                            Toast.makeText(this, "Berhasil menyimpan Draft", Toast.LENGTH_SHORT)
                                .show()
                            binding?.textView3?.text = title
                            binding?.description?.text = description
                        } else {
                            mProgressDialog.dismiss()
                            Toast.makeText(this, "Gagal menyimpan Draft", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val intent = Intent(this, HomepageActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
            return true
        }
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        const val EXTRA_DATA = "data"
        const val BAB_LIST = "babList"
        const val NOVEL_ID = "novelId"
        const val WRITER_ID = "writerId"
        const val BAB_NO = "babNo"
    }
}