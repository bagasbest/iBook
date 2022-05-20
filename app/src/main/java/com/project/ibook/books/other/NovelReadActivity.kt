package com.project.ibook.books.other

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.CountDownTimer
import android.view.KeyEvent
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
    private var babNo = 0
    private var coins = 0L
    private var novelId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNovelReadBinding.inflate(layoutInflater)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        setContentView(binding?.root)

        novelId = "" + intent.getStringExtra(NOVEL_ID)
        AddBookToMyRack.addBook(novelId, this, "read")
        babList = intent.getParcelableArrayListExtra(BAB_LIST)!!
        babNo = intent.getIntExtra(BAB_NO, 0)
        coins = intent.getLongExtra(COINS, 0L)
        checkBabNumber(babNo)
        initView(babNo)
        countdownTimer()

        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        binding?.prev?.setOnClickListener {
            if( babList[babNo-1].unlock?.contains(uid) == true) {
                checkPrevOrNextBab("prev")
            } else {
                checkMonetizationStatus("prev")
            }
        }

        binding?.next?.setOnClickListener {
            if( babList[babNo+1].unlock?.contains(uid) == true) {
                checkPrevOrNextBab("next")
            } else {
                checkMonetizationStatus("next")
            }
        }

        binding?.backButton?.setOnClickListener {
            countDownTimer?.cancel()
            onBackPressed()
        }
    }

    private fun checkMonetizationStatus(option: String) {
        if(option == "prev") {
            when (babList[babNo-1].monetization) {
                "0" -> {
                    checkPrevOrNextBab(option)
                }
                "1" -> {
                    sendGoldCoinUserToNovel(option)
                }
                "2" -> {
                    sendGoldAndSilverCoinUserToNovel(option)
                }
                else -> {
                    checkPrevOrNextBab(option)
                }
            }
        } else {
            when (babList[babNo+1].monetization) {
                "0" -> {
                    checkPrevOrNextBab(option)
                }
                "1" -> {
                    sendGoldCoinUserToNovel(option)
                }
                "2" -> {
                    sendGoldAndSilverCoinUserToNovel(option)
                }
                else -> {
                    checkPrevOrNextBab(option)
                }
            }
        }
    }

    private fun sendGoldAndSilverCoinUserToNovel(option: String) {
        val options = arrayOf("Koin Emas", "Koin Perak")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pilihan Menggunakan Koin")
        builder.setItems(options) { dialog: DialogInterface, which: Int ->
            dialog.dismiss()
            when (which) {
                0 -> {
                    sendGoldCoinUserToNovel(option)
                }
                1 -> {
                    sendSilverCoinUserToNovel(option)
                }
            }
        }
        builder.create().show()
    }

    private fun sendGoldCoinUserToNovel(option: String) {
        val mProgressDialog = ProgressDialog(this)
        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...")
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()

        val uid = FirebaseAuth.getInstance().currentUser!!.uid


        FirebaseFirestore
            .getInstance()
            .collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener {
                val goldCoin = it.data!!["goldCoin"] as Long

                if(goldCoin - 1 >= 0) {
                    if(option == "prev") {
                        babList[babNo-1].unlock?.add(uid)
                    } else {
                        babList[babNo+1].unlock?.add(uid)
                    }


                    /// update sisa gold coin
                    FirebaseFirestore
                        .getInstance()
                        .collection("users")
                        .document(uid)
                        .update("goldCoin", goldCoin - 1)

                    /// send coin to novel
                    coins = coins.plus(1)
                    val updateNovel = mapOf(
                        "coins" to coins,
                        "babList" to babList,
                    )
                    FirebaseFirestore
                        .getInstance()
                        .collection("novel")
                        .document(novelId)
                        .update(updateNovel)
                        .addOnCompleteListener { task ->
                            if(task.isSuccessful) {
                                mProgressDialog.dismiss()
                                checkPrevOrNextBab(option)
                            } else {
                                mProgressDialog.dismiss()
                                Toast.makeText(this, "Ups, ada kendala ketika ingin membuka bab!", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    mProgressDialog.dismiss()
                    Toast.makeText(this, "Jumlah koin perak tidak mencukupi!", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun sendSilverCoinUserToNovel(option: String) {
        val mProgressDialog = ProgressDialog(this)
        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...")
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()

        val uid = FirebaseAuth.getInstance().currentUser!!.uid

        FirebaseFirestore
            .getInstance()
            .collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener {
                val silverCoin = it.data!!["silverCoin"] as Long

                if(silverCoin - 1 >= 0) {
                    if(option == "prev") {
                        babList[babNo-1].unlock?.add(uid)
                    } else {
                        babList[babNo+1].unlock?.add(uid)
                    }


                    /// update sisa gold coin
                    FirebaseFirestore
                        .getInstance()
                        .collection("users")
                        .document(uid)
                        .update("silverCoin", silverCoin - 1)

                    /// send coin to novel
                    coins = coins.plus(1)
                    val updateNovel = mapOf(
                        "coins" to coins,
                        "babList" to babList,
                    )
                    FirebaseFirestore
                        .getInstance()
                        .collection("novel")
                        .document(novelId)
                        .update(updateNovel)
                        .addOnCompleteListener { task ->
                            if(task.isSuccessful) {
                                mProgressDialog.dismiss()
                                checkPrevOrNextBab(option)
                            } else {
                                mProgressDialog.dismiss()
                                Toast.makeText(this, "Ups, ada kendala ketika ingin membuka bab!", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    mProgressDialog.dismiss()
                    Toast.makeText(this, "Jumlah koin perak tidak mencukupi!", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun checkPrevOrNextBab(option: String) {
        if(option == "prev") {
            babNo--
            initView(babNo)
            checkBabNumber(babNo)
        } else {
            babNo++
            initView(babNo)
            checkBabNumber(babNo)
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
        const val COINS = "coin"
    }
}