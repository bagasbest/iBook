package com.project.ibook.books.other

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.KeyEvent
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.ibook.books.my_book.add_edit_bab_novel.MyBookBabModel
import com.project.ibook.databinding.ActivityNovelReadBinding
import com.project.ibook.utils.AddBookToMyRack


class NovelReadActivity : AppCompatActivity() {

    private var binding: ActivityNovelReadBinding? = null
    private var babList = ArrayList<MyBookBabModel>()
    private var timeLeftInMillis: Long = 0
    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNovelReadBinding.inflate(layoutInflater)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        setContentView(binding?.root)

        val novelId = intent.getStringExtra(NOVEL_ID)
        AddBookToMyRack.addBook(novelId, this, "read")
        babList = intent.getParcelableArrayListExtra(BAB_LIST)!!
        var babNo = intent.getIntExtra(BAB_NO, 0)
        checkBabNumber(babNo)
        initView(babNo)
        countdownTimer()

        binding?.prev?.setOnClickListener {
            babNo--
            initView(babNo)
            checkBabNumber(babNo)
        }

        binding?.next?.setOnClickListener {
            babNo++
            initView(babNo)
            checkBabNumber(babNo)
        }

        binding?.backButton?.setOnClickListener {
            countDownTimer?.cancel();
            onBackPressed()
        }
    }

    private fun countdownTimer() {
        timeLeftInMillis = 1800000
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(l: Long) {
                timeLeftInMillis = l
            }

            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                getSilverCoin()
                countdownTimer()
                Toast.makeText(this@NovelReadActivity, "Mendapatkan 1 koin perak", Toast.LENGTH_SHORT).show()
            }
        }.start()
    }

    private fun getSilverCoin() {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        FirebaseFirestore
            .getInstance()
            .collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener {
                val silverCoin = it.data!!["silverCoin"] as Long

                FirebaseFirestore
                    .getInstance()
                    .collection("users")
                    .document(uid)
                    .update("silverCoin", silverCoin+1)
            }
    }

    private fun checkBabNumber(babNo: Int) {
        Log.e("tag", babNo.toString())
        Log.e("tag", babList.size.toString())
        if(babNo == 0 && babList.size > 1) {
            binding?.prev?.isEnabled = false
            binding?.next?.isEnabled = true
        } else if (babList.size == 1 ) {
            binding?.prev?.isEnabled = false
            binding?.next?.isEnabled = false
        } else if (babNo == babList.size-1) {
            binding?.prev?.isEnabled = true
            binding?.next?.isEnabled = false
        } else {
            binding?.prev?.isEnabled = true
            binding?.next?.isEnabled = true
        }
    }

    private fun initView(babNo: Int) {
        binding?.textView3?.text = babList[babNo].title
        binding?.description?.text = babList[babNo].description
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            countDownTimer!!.cancel()
            onBackPressed()
            return true
        }
        return false
    }

    companion object {
        const val BAB_LIST = "babList"
        const val BAB_NO = "babNo"
        const val NOVEL_ID = "novelId"
    }
}