package com.project.ibook.books.my_book.add_edit_bab_novel

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.project.ibook.BottomNavigationActivity
import com.project.ibook.R
import com.project.ibook.databinding.ActivityMyBookBabAddBinding

class MyBookBabAddActivity : AppCompatActivity() {

    private var binding: ActivityMyBookBabAddBinding? = null
    private var babList: ArrayList<MyBookBabModel>? = null
    private var newBabList = ArrayList<MyBookBabModel>()
    private var isReadMode = false
    private var babStatus: String? = null
    private var wCount = 0


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyBookBabAddBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        babList = intent.getParcelableArrayListExtra(BAB_LIST)

        showDropdownBabStatus()

        if(babList == null) {
            binding?.textView3?.text = "Menulis Bab 1"
        } else {
            binding?.textView3?.text = "Menulis Bab ${babList?.size}"
        }


        binding?.saveDraft?.setOnClickListener {
            formValidation()
        }

        binding?.pratinjau?.setOnClickListener {
            if(!isReadMode) {
                binding?.writerSide?.visibility = View.GONE
                binding?.description?.visibility = View.VISIBLE
                isReadMode = true
                binding?.pratinjau?.text = "Kembali Ke Editor Mode"
            } else {
                binding?.writerSide?.visibility = View.VISIBLE
                binding?.description?.visibility = View.GONE
                isReadMode = false
                binding?.pratinjau?.text = "Pratinjau Bab"
            }
        }


        binding?.backButton?.setOnClickListener {
           onBackPressed()
        }

        binding?.sinopsis?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(text: Editable?) {
                if(text.toString().trim().isNotEmpty()) {
                    wCount = 0
                    val word = text.toString().trim()
                    wCount += word.split("\\s+".toRegex()).size
                    binding?.wordCount?.text = "$wCount / 1200 Kata"
                } else {
                    wCount = 0
                    binding?.wordCount?.text = "0 / 1200 Kata"
                }
            }

        })
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
            babStatus == null -> {
                Toast.makeText(this, "Anda harus memilih status bab ini", Toast.LENGTH_SHORT).show()
            }
            babStatus == "Published" && wCount < 1000 -> {
                Toast.makeText(this, "Isi bab minimal 1000 kata untuk bisa di publish!", Toast.LENGTH_SHORT).show()
            }
            wCount > 1200 -> {
                Toast.makeText(this, "Isi bab maksimal 1200 kata!", Toast.LENGTH_SHORT).show()
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
                    monetization = "0",

                )


                if(babList == null) {
                    newBabList.add(model)
                } else {
                    newBabList.addAll(babList!!)
                    newBabList.add(model)
                }


                FirebaseFirestore
                    .getInstance()
                    .collection("novel")
                    .document(intent.getStringExtra(NOVEL_ID)!!)
                    .update("babList", newBabList)
                    .addOnCompleteListener {
                        if(it.isSuccessful) {
                            mProgressDialog.dismiss()
                            Toast.makeText(this, "Berhasil menyimpan Draft", Toast.LENGTH_SHORT).show()
                            binding?.textView3?.text = title
                            binding?.writerSide?.visibility = View.GONE
                            isReadMode = true
                            binding?.description?.text = description
                        } else {
                            mProgressDialog.dismiss()
                            Toast.makeText(this, "Gagal menyimpan Draft", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        const val BAB_LIST = "babList"
        const val NOVEL_ID = "novelId"
    }
}