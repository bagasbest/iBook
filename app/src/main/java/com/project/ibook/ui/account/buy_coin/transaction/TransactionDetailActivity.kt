package com.project.ibook.ui.account.buy_coin.transaction

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.ibook.R
import com.project.ibook.databinding.ActivityTransactionDetailBinding

class TransactionDetailActivity : AppCompatActivity() {

    private var binding: ActivityTransactionDetailBinding? = null
    private var model: TransactionModel? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        checkRole()
        model = intent.getParcelableExtra(EXTRA_DATA)
        Glide.with(this)
            .load(model!!.paymentProof)
            .into(binding!!.paymentProof)

        binding?.coin?.text = "Pembelian: ${model?.coin} Koin"
        binding?.price?.text = "Nominal: ${model?.price}"
        binding?.username?.text = "Atas Nama: ${model?.username}"
        binding?.paymentMethod?.text = "Metode Pembayaran: ${model?.paymentMethod}"
        binding?.status?.text = "Status: ${model?.status}"

        binding?.backButton?.setOnClickListener {
            onBackPressed()
        }

        binding?.acc?.setOnClickListener {
            showConfirmDialog("Menerima")
        }

        binding?.decline?.setOnClickListener {
            showConfirmDialog("Menolak")
        }
    }

    private fun showConfirmDialog(option: String) {
        AlertDialog.Builder(this)
            .setTitle("Konfirmasi $option Pembayaran")
            .setMessage("Apakah anda yakin ingin $option Pembayaran ini ?")
            .setIcon(R.drawable.ic_baseline_warning_24)
            .setPositiveButton("YA") { dialogInterface, _ ->
                dialogInterface.dismiss()
                binding?.progressBar?.visibility = View.VISIBLE
                if(option == "Menerima") {
                    accPayment()
                } else {
                    declinePayment()
                }
            }
            .setNegativeButton("TIDAK", null)
            .show()
    }

    @SuppressLint("SetTextI18n")
    private fun accPayment() {
        FirebaseFirestore
            .getInstance()
            .collection("transaction")
            .document(model?.uid!!)
            .update("status", "Pembayaran Diterima")
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    binding?.acc?.visibility = View.GONE
                    binding?.decline?.visibility = View.GONE
                    binding?.status?.text = "Pembayaran Diterima"
                    addCoinToUser()
                    showSuccessDialog("Berhasil Menerima Pembayaran", "Koin otomatis ditambahkan ke akun pengguna!")
                } else {
                    showFailureDialog("Gagal Menerima Pembayaran")
                }
            }
    }

    private fun addCoinToUser() {
        FirebaseFirestore
            .getInstance()
            .collection("users")
            .document(model?.userId!!)
            .get()
            .addOnSuccessListener {
                val goldCoin = it.data!!["goldCoin"] as Long

                FirebaseFirestore
                    .getInstance()
                    .collection("users")
                    .document()
                    .update("goldCoin", goldCoin + model?.coin!!)
            }
    }

    @SuppressLint("SetTextI18n")
    private fun declinePayment() {
        FirebaseFirestore
            .getInstance()
            .collection("transaction")
            .document(model?.uid!!)
            .update("status", "Pembayaran Ditolak")
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    binding?.acc?.visibility = View.GONE
                    binding?.decline?.visibility = View.GONE
                    binding?.status?.text = "Pembayaran Ditolak"
                    showSuccessDialog("Berhasil Menolak Pembayaran", "Pembayaran ini ditolak!")
                } else {
                    showFailureDialog("Gagal Menolak Pembayaran")
                }
            }
    }


    private fun showFailureDialog(title: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage("Ups, sepertinya interet anda bermasalah, coba lagi nanti!")
            .setIcon(R.drawable.ic_baseline_clear_24)
            .setPositiveButton("OKE") { dialogInterface, _ ->
                binding?.progressBar?.visibility = View.GONE
                dialogInterface.dismiss()
            }
            .show()
    }

    private fun showSuccessDialog(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setIcon(R.drawable.ic_baseline_check_circle_outline_24)
            .setPositiveButton("OKE") { dialogInterface, _ ->
                binding?.progressBar?.visibility = View.GONE
                dialogInterface.dismiss()
                onBackPressed()
            }
            .show()
    }


    private fun checkRole() {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        FirebaseFirestore
            .getInstance()
            .collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener {
                if("" + it.data!!["role"] == "admin" && "" + it.data!!["status"] == "Menunggu Verifikasi") {
                    binding?.acc?.visibility = View.VISIBLE
                    binding?.decline?.visibility = View.VISIBLE
                }
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