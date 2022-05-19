package com.project.ibook.books.write

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.project.ibook.R
import com.project.ibook.databinding.ActivityWriteBinding
import java.util.*
import kotlin.collections.ArrayList

class WriteActivity : AppCompatActivity() {

    private var binding: ActivityWriteBinding? = null
    private var image: String? = null
    private val REQUEST_IMAGE_GALLERY = 1001
    private var writerName: String? = null
    private var writerId: String? = null
    private var genreList = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        getUserInfo()

        binding?.backButton?.setOnClickListener {
            onBackPressed()
        }

        binding?.saveBtn?.setOnClickListener {
            formValidation()
        }

        binding?.imageHint?.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .compress(1024)
                .start(REQUEST_IMAGE_GALLERY)
        }
    }

    private fun getUserInfo() {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        writerId = uid
        FirebaseFirestore
            .getInstance()
            .collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener {
                writerName = it.data!!["name"].toString()
            }
    }

    private fun formValidation() {

        val title = binding?.title?.text.toString().trim()
        val synopsis = binding?.sinopsis?.text.toString().trim()

        when {
            title.isEmpty() -> {
                Toast.makeText(this, "Judul novel tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
            genreList.size == 0 -> {
                Toast.makeText(this, "Silahkan memilih minimal 1 genre novel", Toast.LENGTH_SHORT).show()
            }
            synopsis.isEmpty() -> {
                Toast.makeText(this, "Sinopsis novel tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
            image == null -> {
                Toast.makeText(this, "Cover gambar novel tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
            else -> {
                val mProgressDialog = ProgressDialog(this)
                mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...")
                mProgressDialog.setCanceledOnTouchOutside(false)
                mProgressDialog.show()

                val uid = System.currentTimeMillis().toString()
                /// monetization 0 = free reading, 1 = using goldCoin only, 2 = using goldCoin & silverCoin
                val data = mapOf(
                    "uid" to uid,
                    "title" to title,
                    "titleTemp" to title.lowercase(Locale.getDefault()),
                    "genre" to genreList,
                    "synopsis" to synopsis,
                    "image" to image,
                    "viewTime" to 0L,
                    "writerName" to writerName,
                    "writerUid" to writerId,
                    "status" to "Draft",
                    "homepageCategory" to "",
                    "coins" to 0L,
                )

                FirebaseFirestore
                    .getInstance()
                    .collection("novel")
                    .document(uid)
                    .set(data)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            mProgressDialog.dismiss()
                            showSuccessDialog()
                        } else {
                            mProgressDialog.dismiss()
                            showFailureDialog()
                        }
                    }
            }
        }

    }

    /// munculkan dialog ketika gagal registrasi
    private fun showFailureDialog() {
        AlertDialog.Builder(this)
            .setTitle("Gagal Membuat Novel")
            .setMessage("Ups, sepertinya koneksi internet anda tidak stabil, silahkan coba lagi nanti")
            .setIcon(R.drawable.ic_baseline_clear_24)
            .setPositiveButton("OKE") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .show()
    }

    /// munculkan dialog ketika sukses registrasi
    private fun showSuccessDialog() {
        AlertDialog.Builder(this)
            .setTitle("Berhasil Membuat Novel")
            .setMessage("Silahkan kehalaman Novel Saya, dan klik judul novel yang tersedia untuk mulai menulis isi novel!")
            .setIcon(R.drawable.ic_baseline_check_circle_outline_24)
            .setPositiveButton("OKE") { dialogInterface, _ ->
                dialogInterface.dismiss()
                onBackPressed()
            }
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_GALLERY) {
                uploadArticleDp(data?.data)
            }
        }
    }


    /// fungsi untuk mengupload foto kedalam cloud storage
    private fun uploadArticleDp(data: Uri?) {
        val mStorageRef = FirebaseStorage.getInstance().reference
        val mProgressDialog = ProgressDialog(this)
        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...")
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()
        val imageFileName = "cover/image_" + System.currentTimeMillis() + ".png"
        mStorageRef.child(imageFileName).putFile(data!!)
            .addOnSuccessListener {
                mStorageRef.child(imageFileName).downloadUrl
                    .addOnSuccessListener { uri: Uri ->
                        mProgressDialog.dismiss()
                        image = uri.toString()
                        Glide.with(this)
                            .load(image)
                            .into(binding!!.image)
                    }
                    .addOnFailureListener { e: Exception ->
                        mProgressDialog.dismiss()
                        Toast.makeText(
                            this,
                            "Gagal mengunggah gambar",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("imageDp: ", e.toString())
                    }
            }
            .addOnFailureListener { e: Exception ->
                mProgressDialog.dismiss()
                Toast.makeText(
                    this,
                    "Gagal mengunggah gambar",
                    Toast.LENGTH_SHORT
                )
                    .show()
                Log.d("imageDp: ", e.toString())
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    fun onCheckboxClicked(view: View) {
        if (view is CheckBox) {
            val checked: Boolean = view.isChecked

            when (view.id) {
                R.id.cb1 -> {
                    if (checked) {
                        genreList.add(binding?.cb1?.text.toString())
                    } else {
                        genreList.remove(binding?.cb1?.text.toString());
                    }
                }
                R.id.cb2 -> {
                    if (checked) {
                        genreList.add(binding?.cb2?.text.toString())
                    } else {
                        genreList.remove(binding?.cb2?.text.toString());
                    }
                }
                R.id.cb3 -> {
                    if (checked) {
                        genreList.add(binding?.cb3?.text.toString())
                    } else {
                        genreList.remove(binding?.cb3?.text.toString());
                    }
                }
                R.id.cb4 -> {
                    if (checked) {
                        genreList.add(binding?.cb4?.text.toString())
                    } else {
                        genreList.remove(binding?.cb4?.text.toString());
                    }
                }
                R.id.cb5 -> {
                    if (checked) {
                        genreList.add(binding?.cb5?.text.toString())
                    } else {
                        genreList.remove(binding?.cb5?.text.toString());
                    }
                }
                R.id.cb6 -> {
                    if (checked) {
                        genreList.add(binding?.cb6?.text.toString())
                    } else {
                        genreList.remove(binding?.cb6?.text.toString());
                    }
                }
                R.id.cb7 -> {
                    if (checked) {
                        genreList.add(binding?.cb7?.text.toString())
                    } else {
                        genreList.remove(binding?.cb7?.text.toString());
                    }
                }

                R.id.cb8 -> {
                    if (checked) {
                        genreList.add(binding?.cb8?.text.toString())
                    } else {
                        genreList.remove(binding?.cb8?.text.toString());
                    }
                }
            }
        }
    }
}