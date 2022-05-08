package com.project.ibook.books.my_book

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.project.ibook.HomepageActivity
import com.project.ibook.R
import com.project.ibook.databinding.ActivityMyBookEditBinding

class MyBookEditActivity : AppCompatActivity() {

    private var binding: ActivityMyBookEditBinding? = null
    private var model: MyBookModel? = null
    private var image: String? = null
    private val REQUEST_IMAGE_GALLERY = 1001
    private var novelStatus : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyBookEditBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        model = intent.getParcelableExtra(EXTRA_DATA)
        novelStatus = model?.status
        image = model?.image
        Glide.with(this)
            .load(image)
            .into(binding!!.image)

        binding?.title?.setText(model?.title)
        binding?.sinopsis?.setText(model?.synopsis)
        binding?.genre?.setText(model?.genre)

        showDropdownNovelStatus()

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

    private fun showDropdownNovelStatus() {
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.novel_status, android.R.layout.simple_list_item_1
        )
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        binding?.novelStatus?.setAdapter(adapter)
        binding?.novelStatus?.setOnItemClickListener { _, _, _, _ ->
           val status = binding?.novelStatus!!.text.toString()
            novelStatus = if(status == "Draft (Novel belum di publikasikan)") {
                "Draft"
            } else {
                "Published"
            }
        }
    }

    private fun formValidation() {
        val title = binding?.title?.text.toString().trim()
        val genre = binding?.genre?.text.toString().trim()
        val synopsis = binding?.sinopsis?.text.toString().trim()

        when {
            novelStatus == "Published" && model?.babList == null -> {
                Toast.makeText(this, "Anda harus mengunggah setidaknya 1 Bab, untuk mempublikasikan novel ini", Toast.LENGTH_SHORT).show()
            }
            title.isEmpty() -> {
                Toast.makeText(this, "Judul novel tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
            genre.isEmpty() -> {
                Toast.makeText(this, "Genre novel tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
            synopsis.isEmpty() -> {
                Toast.makeText(this, "Sinopsis novel tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
            else -> {
                val mProgressDialog = ProgressDialog(this)
                mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...")
                mProgressDialog.setCanceledOnTouchOutside(false)
                mProgressDialog.show()

                val data = mapOf(
                    "title" to title,
                    "genre" to genre,
                    "synopsis" to synopsis,
                    "image" to image,
                    "status" to novelStatus,
                )

                FirebaseFirestore
                    .getInstance()
                    .collection("novel")
                    .document(model?.uid!!)
                    .update(data)
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

    private fun showFailureDialog() {
        AlertDialog.Builder(this)
            .setTitle("Gagal Memperbarui Novel")
            .setMessage("Ups, sepertinya koneksi internet anda tidak stabil, silahkan coba lagi nanti")
            .setIcon(R.drawable.ic_baseline_clear_24)
            .setPositiveButton("OKE") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .show()
    }

    private fun showSuccessDialog() {
        AlertDialog.Builder(this)
            .setTitle("Berhasil Memperbarui Novel")
            .setMessage("Novel anda berhasil di perbarui")
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

    companion object {
        const val EXTRA_DATA = "data"
    }
}