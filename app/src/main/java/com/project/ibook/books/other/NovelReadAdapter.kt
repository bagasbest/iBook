package com.project.ibook.books.other

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.ibook.books.my_book.add_edit_bab_novel.MyBookBabModel
import com.project.ibook.databinding.ItemBabBinding
import com.project.ibook.ui.account.buy_coin.BuyCoinDashboardActivity

class NovelReadAdapter(
    private var babList: ArrayList<MyBookBabModel>,
    private val novelId: String?,
    private val role: String?,
    private var coins: Long?,
) : RecyclerView.Adapter<NovelReadAdapter.ViewHolder>() {


    inner class ViewHolder(private val binding: ItemBabBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(model: MyBookBabModel, position: Int) {
            with(binding) {

                val uid = FirebaseAuth.getInstance().currentUser!!.uid
                titleBab.text = model.title
                descBab.text = model.description

                if(role == "admin") {
                    menu.visibility = View.VISIBLE
                }


                /// 0 : Free Bab, tidak membutuhkan koin, langsung bisa membaca bab novel
                /// 1 : Gold Coin, Membutuhkan gold coin untuk membaca
                /// 2 : Gold & Silver Coin, membutuhkan gold / silver coin untuk membaca

                when (model.monetization) {
                    "0" -> {
                        binding.goldCoin.visibility = View.GONE
                        binding.silverCoin.visibility = View.GONE
                    }
                    "1" -> {
                        binding.goldCoin.visibility = View.VISIBLE
                        binding.silverCoin.visibility = View.GONE
                    }
                    "2" -> {
                        binding.goldCoin.visibility = View.VISIBLE
                        binding.silverCoin.visibility = View.VISIBLE
                    }
                }

                cv.setOnClickListener {
                    when (model.monetization) {
                        "0" -> {
                            binding.goldCoin.visibility = View.GONE
                            binding.silverCoin.visibility = View.GONE

                            val intent = Intent(itemView.context, NovelReadActivity::class.java)
                            intent.putExtra(NovelReadActivity.BAB_LIST, babList)
                            intent.putExtra(NovelReadActivity.BAB_NO, position)
                            intent.putExtra(NovelReadActivity.NOVEL_ID, novelId)
                            intent.putExtra(NovelReadActivity.COINS, coins)
                            itemView.context.startActivity(intent)
                        }
                        "1" -> {
                            binding.goldCoin.visibility = View.VISIBLE
                            binding.silverCoin.visibility = View.GONE

                            if(model.unlock?.contains(uid) == true) {
                                val intent = Intent(itemView.context, NovelReadActivity::class.java)
                                intent.putExtra(NovelReadActivity.BAB_LIST, babList)
                                intent.putExtra(NovelReadActivity.BAB_NO, position)
                                intent.putExtra(NovelReadActivity.NOVEL_ID, novelId)
                                intent.putExtra(NovelReadActivity.COINS, coins)
                                itemView.context.startActivity(intent)
                            } else {
                                sendGoldCoinUserToNovel(itemView.context, position)
                            }
                        }
                        "2" -> {
                            binding.goldCoin.visibility = View.VISIBLE
                            binding.silverCoin.visibility = View.VISIBLE

                            if(model.unlock?.contains(uid) == true) {
                                val intent = Intent(itemView.context, NovelReadActivity::class.java)
                                intent.putExtra(NovelReadActivity.BAB_LIST, babList)
                                intent.putExtra(NovelReadActivity.BAB_NO, position)
                                intent.putExtra(NovelReadActivity.NOVEL_ID, novelId)
                                intent.putExtra(NovelReadActivity.COINS, coins)
                                itemView.context.startActivity(intent)
                            }  else {
                                chooseGoldOrSilverCoins(itemView.context, position)
                            }

                        }
                        else -> {
                            val intent = Intent(itemView.context, NovelReadActivity::class.java)
                            intent.putExtra(NovelReadActivity.BAB_LIST, babList)
                            intent.putExtra(NovelReadActivity.BAB_NO, position)
                            intent.putExtra(NovelReadActivity.NOVEL_ID, novelId)
                            intent.putExtra(NovelReadActivity.COINS, coins)
                            itemView.context.startActivity(intent)
                        }
                    }
                }

                menu.setOnClickListener {
                    val options = arrayOf("Tidak Ada Monetisasi", "Monetisasi Koin Emas", "Monetisasi Koin Emas & Perak")

                    val builder = AlertDialog.Builder(itemView.context)
                    builder.setTitle("Monetisasi Bab Ini")
                    builder.setItems(options) { dialog: DialogInterface, which: Int ->
                        dialog.dismiss()
                        when (which) {
                            0 -> {
                              updateMonetization("0", goldCoin, silverCoin, position, itemView.context)
                            }
                            1 -> {
                                updateMonetization("1", goldCoin, silverCoin, position,  itemView.context)
                            }
                            2 -> {
                                updateMonetization("2",  goldCoin, silverCoin, position, itemView.context)
                            }
                        }
                    }
                    builder.create().show()
                }
            }
        }

    }

    private fun chooseGoldOrSilverCoins(
        context: Context,
        position: Int,
    ) {
        val options = arrayOf("Koin Emas", "Koin Perak")

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Pilihan Menggunakan Koin")
        builder.setItems(options) { dialog: DialogInterface, which: Int ->
            dialog.dismiss()
            when (which) {
                0 -> {
                    sendGoldCoinUserToNovel(context, position)
                }
                1 -> {
                    sendSilverCoinUserToNovel(context, position)
                }
            }
        }
        builder.create().show()
    }

    private fun sendSilverCoinUserToNovel(
        context: Context,
        position: Int,
    ) {

        val mProgressDialog = ProgressDialog(context)
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
                    babList[position].unlock?.add(uid)

                    /// update sisa gold coin
                    FirebaseFirestore
                        .getInstance()
                        .collection("users")
                        .document(uid)
                        .update("silverCoin", silverCoin - 1)

                    /// send coin to novel
                    coins = coins?.plus(1)
                    val updateNovel = mapOf(
                        "coins" to coins,
                        "babList" to babList,
                    )
                    FirebaseFirestore
                        .getInstance()
                        .collection("novel")
                        .document(novelId!!)
                        .update(updateNovel)
                        .addOnCompleteListener { task ->
                            if(task.isSuccessful) {
                                mProgressDialog.dismiss()
                                val intent = Intent(context, NovelReadActivity::class.java)
                                intent.putExtra(NovelReadActivity.BAB_LIST, babList)
                                intent.putExtra(NovelReadActivity.BAB_NO, position)
                                intent.putExtra(NovelReadActivity.NOVEL_ID, novelId)
                                intent.putExtra(NovelReadActivity.COINS, coins)
                                context.startActivity(intent)
                            } else {
                                mProgressDialog.dismiss()
                                Toast.makeText(context, "Ups, ada kendala ketika ingin membuka bab!", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    mProgressDialog.dismiss()
                    Toast.makeText(context, "Silahkan membeli koin untuk membaca bab ini!", Toast.LENGTH_SHORT).show()
                    context.startActivity(Intent(context, BuyCoinDashboardActivity::class.java))
                }
            }
    }

    private fun sendGoldCoinUserToNovel(
        context: Context,
        position: Int,
    ) {

        val mProgressDialog = ProgressDialog(context)
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
                    babList[position].unlock?.add(uid)
                    /// update sisa gold coin
                    FirebaseFirestore
                        .getInstance()
                        .collection("users")
                        .document(uid)
                        .update("goldCoin", goldCoin - 1)

                    /// send coin to novel
                    coins = coins?.plus(1)

                    val updateNovel = mapOf(
                        "coins" to coins,
                        "babList" to babList,
                    )

                    FirebaseFirestore
                        .getInstance()
                        .collection("novel")
                        .document(novelId!!)
                        .update(updateNovel)
                        .addOnCompleteListener { task ->
                            if(task.isSuccessful) {
                                mProgressDialog.dismiss()
                                val intent = Intent(context, NovelReadActivity::class.java)
                                intent.putExtra(NovelReadActivity.BAB_LIST, babList)
                                intent.putExtra(NovelReadActivity.BAB_NO, position)
                                intent.putExtra(NovelReadActivity.NOVEL_ID, novelId)
                                intent.putExtra(NovelReadActivity.COINS, coins)
                                context.startActivity(intent)
                            } else {
                                mProgressDialog.dismiss()
                                Toast.makeText(context, "Ups, ada kendala ketika ingin membuka bab!", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    mProgressDialog.dismiss()
                    Toast.makeText(context, "Silahkan membeli koin untuk membaca bab ini!", Toast.LENGTH_SHORT).show()
                    context.startActivity(Intent(context, BuyCoinDashboardActivity::class.java))
                }
            }
    }

    private fun updateMonetization(
        option: String,
        goldCoin: ImageView,
        silverCoin: ImageView,
        position: Int,
        context: Context
    ) {

        babList[position].monetization = option

        FirebaseFirestore
            .getInstance()
            .collection("novel")
            .document(novelId!!)
            .update("babList", babList)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    when (option) {
                        "0" -> {
                            goldCoin.visibility = View.GONE
                            silverCoin.visibility = View.GONE
                        }
                        "1" -> {
                            goldCoin.visibility = View.VISIBLE
                            silverCoin.visibility = View.GONE
                        }
                        "2" -> {
                            goldCoin.visibility = View.VISIBLE
                            silverCoin.visibility = View.VISIBLE
                        }
                    }
                    Toast.makeText(context, "Berhasil memonetisasi novel!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Gagal memonetisasi novel!", Toast.LENGTH_SHORT).show()
                }
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBabBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(babList[position], position)
    }

    override fun getItemCount(): Int = babList.size
}