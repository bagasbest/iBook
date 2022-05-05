package com.project.ibook.books.my_book

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.ibook.books.write.WriteNovelActivity
import com.project.ibook.databinding.ItemMyNovelBinding

class MyBookAdapter : RecyclerView.Adapter<MyBookAdapter.ViewHolder>() {

    private val bookList = ArrayList<MyBookModel>()
    @SuppressLint("NotifyDataSetChanged")
    fun setData(items: ArrayList<MyBookModel>) {
        bookList.clear()
        bookList.addAll(items)
        notifyDataSetChanged()
    }


    inner class ViewHolder(private val binding: ItemMyNovelBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(model: MyBookModel) {
            with(binding) {

                title.text = model.title
                description.text = model.synopsis
                status.text = model.status
                Glide.with(itemView.context)
                    .load(model.image)
                    .into(image)


                if(model.status == "Draft") {
                    backgroudStatus.backgroundTintList = ContextCompat.getColorStateList(itemView.context, android.R.color.holo_red_light)
                } else {
                    backgroudStatus.backgroundTintList = ContextCompat.getColorStateList(itemView.context, android.R.color.holo_green_dark)
                }

                cv.setOnClickListener {
                    val intent = Intent(itemView.context, WriteNovelActivity::class.java)
                    intent.putExtra(WriteNovelActivity.EXTRA_DATA, model)
                    itemView.context.startActivity(intent)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMyNovelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(bookList[position])
    }

    override fun getItemCount(): Int = bookList.size
}