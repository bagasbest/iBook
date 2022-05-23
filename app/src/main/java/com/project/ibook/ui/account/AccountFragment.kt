package com.project.ibook.ui.account

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.project.ibook.MainActivity
import com.project.ibook.R
import com.project.ibook.books.my_book.MyBookActivity
import com.project.ibook.books.write.WriteActivity
import com.project.ibook.databinding.FragmentAccountBinding
import com.project.ibook.ui.account.buy_coin.BuyCoinDashboardActivity
import java.text.DecimalFormat

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private var image: String? = null
    private val REQUEST_IMAGE_GALLERY = 1001
    private var uid: String? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAccountBinding.inflate(inflater, container, false)

        if(FirebaseAuth.getInstance().currentUser != null) {
            binding.noLogin.visibility = View.GONE
            binding.content.visibility = View.VISIBLE
            uid = FirebaseAuth.getInstance().currentUser!!.uid
            retrieveUserData()
        }

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun retrieveUserData() {
        FirebaseFirestore
            .getInstance()
            .collection("users")
            .document(uid!!)
            .get()
            .addOnSuccessListener {
                val formatter = DecimalFormat("#,###")
                val username = "" + it.data!!["username"]
                val email = "" + it.data!!["email"]
                val image = "" + it.data!!["image"]
                val goldCoin = it.data!!["goldCoin"] as Long
                val silverCoin = it.data!!["silverCoin"] as Long


                if(image != "") {
                    Glide.with(requireContext())
                        .load(image)
                        .into(binding.image)
                } else {
                    Glide.with(requireContext())
                        .load(R.drawable.ic_baseline_face_24_2)
                        .into(binding.image)
                }

                binding.username.text = username
                binding.email.text = email
                binding.goldCoin.text = formatter.format(goldCoin)
                binding.silverCoin.text = formatter.format(silverCoin)
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.myBook.setOnClickListener {
            startActivity(Intent(activity, MyBookActivity::class.java))
        }

        binding.textView10.setOnClickListener {
            startActivity(Intent(activity, WriteActivity::class.java))
        }

        binding.imageHint.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .compress(1024)
                .start(REQUEST_IMAGE_GALLERY)
        }

        binding.loginBtn.setOnClickListener {
            startActivity(Intent(activity, MainActivity::class.java))
        }

        binding.button.setOnClickListener {
            startActivity(Intent(activity, BuyCoinDashboardActivity::class.java))
        }

        binding.textView13.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com/koalanovel.id?igshid=YmMyMTA2M2Y"))
            startActivity(browserIntent)
        }

        binding.textView11.setOnClickListener {
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Hai, jika kamu ingin membaca kumpulan novel terbaik, silahkan download aplikasi KoalaNovel di Playstore!")
            sendIntent.type = "text/plain"
            startActivity(sendIntent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_GALLERY) {
                Glide.with(requireContext())
                    .load(data?.data)
                    .into(binding.image)
               uploadArticleDp(data?.data)
            }
        }
    }


    /// fungsi untuk mengupload foto kedalam cloud storage
    private fun uploadArticleDp(data: Uri?) {
        val mStorageRef = FirebaseStorage.getInstance().reference
        val mProgressDialog = ProgressDialog(activity)
        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...")
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()
        val imageFileName = "users/image_" + System.currentTimeMillis() + ".png"
        mStorageRef.child(imageFileName).putFile(data!!)
            .addOnSuccessListener {
                mStorageRef.child(imageFileName).downloadUrl
                    .addOnSuccessListener { uri: Uri ->
                        mProgressDialog.dismiss()
                        image = uri.toString()
                        updateImageInDB()
                    }
                    .addOnFailureListener { e: Exception ->
                        mProgressDialog.dismiss()
                        Toast.makeText(
                            activity,
                            "Gagal mengunggah gambar",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("imageDp: ", e.toString())
                    }
            }
            .addOnFailureListener { e: Exception ->
                mProgressDialog.dismiss()
                Toast.makeText(
                    activity,
                    "Gagal mengunggah gambar",
                    Toast.LENGTH_SHORT
                )
                    .show()
                Log.d("imageDp: ", e.toString())
            }
    }

    private fun updateImageInDB() {
        FirebaseFirestore
            .getInstance()
            .collection("users")
            .document(uid!!)
            .update("image",image)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}