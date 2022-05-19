package com.project.ibook.ui.account.buy_coin.transaction

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.project.ibook.R
import com.project.ibook.databinding.ItemTransactionBinding

class TransactionAdapter : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    private val transactionList = ArrayList<TransactionModel>()
    @SuppressLint("NotifyDataSetChanged")
    fun setData(items: ArrayList<TransactionModel>) {
        transactionList.clear()
        transactionList.addAll(items)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemTransactionBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(model: TransactionModel) {
            with(binding) {

                coin.text = "Pembelian: ${model.coin} Koin"
                username.text = "Atas Nama: ${model.username}"
                price.text = "Nominal: ${model.price}"
                status.text = model.status

                when (model.status) {
                    "Pembayaran Diterima" -> {
                        background.backgroundTintList = ContextCompat.getColorStateList(itemView.context, android.R.color.holo_green_dark)
                    }
                    "Pembayaran Ditolak" -> {
                        background.backgroundTintList = ContextCompat.getColorStateList(itemView.context, android.R.color.holo_red_light)
                    }
                    else -> {
                        background.backgroundTintList = ContextCompat.getColorStateList(itemView.context, R.color.orange)
                    }
                }

                cv.setOnClickListener {
                    val intent = Intent(itemView.context, TransactionDetailActivity::class.java)
                    intent.putExtra(TransactionDetailActivity.EXTRA_DATA, model)
                    itemView.context.startActivity(intent)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(transactionList[position])
    }

    override fun getItemCount(): Int = transactionList.size
}