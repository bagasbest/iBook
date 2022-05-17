package com.project.ibook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.himanshurawat.hasher.HashType
import com.himanshurawat.hasher.Hasher
import com.project.ibook.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        binding?.registerGuest?.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding?.login?.setOnClickListener {
            formValidation()
        }


    }

    private fun formValidation() {
        val username = binding?.emailOrPhone?.text.toString().trim()
        val password = binding?.password?.text.toString().trim()

        when {
            username.isEmpty() -> {
                Toast.makeText(this, "Username tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
            password.isEmpty() -> {
                Toast.makeText(this, "Kata sandi tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
            else -> {
                binding?.progressBar?.visibility = View.VISIBLE
                FirebaseFirestore
                    .getInstance()
                    .collection("users")
                    .whereEqualTo("username", username)
                    .whereEqualTo("password", Hasher.hash(password, HashType.SHA_512))
                    .get()
                    .addOnSuccessListener {
                        if(it.size() > 0) {
                            val email = "" + it.documents[0]["email"]

                            FirebaseAuth.getInstance()
                                .signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    if(task.isSuccessful) {
                                        binding?.progressBar?.visibility = View.GONE
                                        startActivity(Intent(this, BottomNavigationActivity::class.java))
                                        finish()
                                    } else {
                                        binding?.progressBar?.visibility = View.GONE
                                        showFailureDialog()
                                    }
                                }
                        } else {
                            binding?.progressBar?.visibility = View.GONE
                            showFailureDialog()
                        }
                    }
            }
        }
    }


    private fun showFailureDialog() {
        AlertDialog.Builder(this)
            .setTitle("Gagal melakukan login")
            .setMessage("Ups, sepertinya koneksi internet anda tidak stabil, silahkan coba lagi nanti")
            .setIcon(R.drawable.ic_baseline_clear_24)
            .setPositiveButton("OKE") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}