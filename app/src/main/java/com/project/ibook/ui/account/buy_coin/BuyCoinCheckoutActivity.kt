package com.project.ibook.ui.account.buy_coin

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.project.ibook.R
import com.project.ibook.databinding.ActivityBuyCoinCheckoutBinding
import java.text.SimpleDateFormat
import java.util.*

class BuyCoinCheckoutActivity : AppCompatActivity() {

    private var binding: ActivityBuyCoinCheckoutBinding? = null
    private var paymentMethod: String? = null
    private var image: String? = null
    private val REQUEST_IMAGE_GALLERY = 1001

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuyCoinCheckoutBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        binding?.coin?.text = "Pembelian: ${intent.getIntExtra(COIN, 0)} Koin Emas"
        binding?.price?.text = "Nominal: ${intent.getStringExtra(PRICE)}"
        showDropdownHomepageCategory()
        binding?.backButton?.setOnClickListener {
            onBackPressed()
        }

        binding?.imageHint?.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start(REQUEST_IMAGE_GALLERY)
        }


    }

    @SuppressLint("SetTextI18n")
    private fun showDropdownHomepageCategory() {
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.payment_method, android.R.layout.simple_list_item_1
        )
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        binding?.paymentMethod?.setAdapter(adapter)
        binding?.paymentMethod?.setOnItemClickListener { _, _, _, _ ->
            paymentMethod = binding?.paymentMethod!!.text.toString()

            if(paymentMethod == "Bank BCA") {
                binding?.proofView?.visibility = View.VISIBLE
                binding?.paymentBank?.text = "Bank: Bank BCA"
                binding?.paymentNumber?.text = "No.Rekening: 123-4556-212"
                binding?.paymentName?.text = "Atas Nama: Admin KoalaNovel"
            } else if (paymentMethod == "GO-PAY") {
                binding?.proofView?.visibility = View.VISIBLE
                binding?.paymentBank?.text = "E-Wallet: GO-PAY"
                binding?.paymentNumber?.text = "No.Hanphone: 0812 3456 7890"
                binding?.paymentName?.visibility = View.GONE
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_GALLERY) {
                Glide.with(this)
                    .load(data?.data)
                    .into(binding!!.image)
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
        val imageFileName = "payment_proof/image_" + System.currentTimeMillis() + ".png"
        mStorageRef.child(imageFileName).putFile(data!!)
            .addOnSuccessListener {
                mStorageRef.child(imageFileName).downloadUrl
                    .addOnSuccessListener { uri: Uri ->
                        image = uri.toString()
                        updateImageInDB(mProgressDialog)
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

    private fun updateImageInDB(mProgressDialog: ProgressDialog) {

        val myUid = FirebaseAuth.getInstance().currentUser!!.uid
        FirebaseFirestore
            .getInstance()
            .collection("users")
            .document(myUid)
            .get()
            .addOnSuccessListener {

                val uid = System.currentTimeMillis().toString()
                val c: Calendar = Calendar.getInstance()
                val df = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
                val formattedDate: String = df.format(c.time)
                val coin = intent.getIntExtra(COIN, 0)
                val price = intent.getStringExtra(PRICE)

                val data = mapOf(
                    "uid" to uid,
                    "userId" to "" + it.data!!["uid"],
                    "username" to "" + it.data!!["username"],
                    "date" to formattedDate,
                    "status" to "Menunggu Verifikasi",
                    "coin" to coin.toLong(),
                    "price" to price,
                    "paymentProof" to image,
                    "paymentMethod" to paymentMethod
                )

                FirebaseFirestore
                    .getInstance()
                    .collection("transaction")
                    .document(uid)
                    .set(data)
                    .addOnCompleteListener { task ->
                        if(task.isSuccessful) {
                            mProgressDialog.dismiss()
                            showSuccessDialog()
                        } else {
                            mProgressDialog.dismiss()
                            showFailureDialog()
                        }
                    }
            }
    }

    private fun showFailureDialog() {
        AlertDialog.Builder(this)
            .setTitle("Gagal Unggah Bukti Pembayaran")
            .setMessage("Ups, sepertinya terdapat kendala pada internet anda, coba lagi nanti ya!")
            .setIcon(R.drawable.ic_baseline_clear_24)
            .setPositiveButton("OKE") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .show()
    }

    private fun showSuccessDialog() {
        AlertDialog.Builder(this)
            .setTitle("Berhasil Unggah Bukti Pembayaran")
            .setMessage("Bukti pembayaran berhasil di unggah, selanjutnya menunggu verifikasi dari admin KoalaNovel, anda bisa mengecek status pembelian anda pada halaman ''Daftar Transaksi''")
            .setIcon(R.drawable.ic_baseline_check_circle_outline_24)
            .setPositiveButton("OKE") { dialogInterface, _ ->
                dialogInterface.dismiss()
                onBackPressed()
            }
            .show()
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        const val COIN = "coin"
        const val PRICE = "price"
    }
}