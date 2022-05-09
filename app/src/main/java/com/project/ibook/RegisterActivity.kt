package com.project.ibook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import com.himanshurawat.hasher.HashType
import com.himanshurawat.hasher.Hasher
import com.project.ibook.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private var binding: ActivityRegisterBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.backButton?.setOnClickListener {
            onBackPressed()
        }

        binding?.register?.setOnClickListener {
            formValidation()
        }

    }

    private fun formValidation() {
        val name = binding?.name?.text.toString().trim()
        val username = binding?.username?.text.toString().trim()
        val email = binding?.email?.text.toString().trim()
        val password = binding?.password?.text.toString().trim()
        val phone = binding?.phone?.text.toString().trim()

        when {
            name.isEmpty() -> {
                Toast.makeText(this, "Maaf, Nama Lengkap tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
            username.isEmpty() -> {
                Toast.makeText(this, "Maaf, Username tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
            email.isEmpty() -> {
                Toast.makeText(this, "Maaf, Email tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
            password.isEmpty() -> {
                Toast.makeText(this, "Maaf, Kata sandi tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
            password.length < 6 -> {
                Toast.makeText(this, "Maaf, Kata sandi minimal 6 karakter", Toast.LENGTH_SHORT).show()
            }
            phone.isEmpty() -> {
                Toast.makeText(this, "Maaf, Kata sandi tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
            else -> {
                binding?.progressBar?.visibility = View.VISIBLE

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if(it.isSuccessful) {
                                saveDataToFirebase(
                                    name,
                                    username,
                                    email,
                                    password,
                                    phone
                                )
                        } else {
                            binding?.progressBar?.visibility = View.GONE
                            try {
                                throw it.exception!!
                            } catch (e: FirebaseAuthUserCollisionException) {
                                showFailureDialog("Email yang anda daftarkan sudah digunakan, silahkan coba email lain")
                            } catch (e: java.lang.Exception) {
                                Log.e("TAG", e.message!!)
                            }
                        }
                    }
            }
        }

    }

    private fun saveDataToFirebase(
        name: String,
        username: String,
        email: String,
        password: String,
        phone: String
    ) {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val data = mapOf(
            "uid" to uid,
            "name" to name,
            "username" to username,
            "email" to email,
            "password" to Hasher.hash(password, HashType.SHA_512),
            "phone" to phone,
            "role" to "user"
        )

        FirebaseFirestore
            .getInstance()
            .collection("users")
            .document(uid)
            .set(data)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    binding?.progressBar?.visibility = View.GONE
                    showSuccessDialog()
                } else {
                    binding?.progressBar?.visibility = View.GONE
                    showFailureDialog("Silahkan mendaftar kembali dengan informasi yang benar, dan pastikan koneksi internet lancar")
                }
            }

    }

    /// munculkan dialog ketika gagal registrasi
    private fun showFailureDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Gagal melakukan registrasi")
            .setMessage(message)
            .setIcon(R.drawable.ic_baseline_clear_24)
            .setPositiveButton("OKE") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .show()
    }

    /// munculkan dialog ketika sukses registrasi
    private fun showSuccessDialog() {
        AlertDialog.Builder(this)
            .setTitle("Berhasil melakukan registrasi")
            .setMessage("Silahkan login menggunakan username dan kata sandi yang anda daftarkan")
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
}