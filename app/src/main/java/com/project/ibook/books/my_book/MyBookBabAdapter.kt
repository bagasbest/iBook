package com.project.ibook.books.my_book

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.ibook.databinding.ItemBabBinding

class MyBookBabAdapter(
    private var babList: ArrayList<MyBookBabModel>,
    private val novelId: String?,
    private val writerUid: String?
) : RecyclerView.Adapter<MyBookBabAdapter.ViewHolder>() {


    inner class ViewHolder(private val binding: ItemBabBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(model: MyBookBabModel) {
            with(binding) {

                titleBab.text = model.title
                descBab.text = model.description

                cv.setOnClickListener {
                    val intent = Intent(itemView.context, MyBookBabDetailActivity::class.java)
                    intent.putExtra(MyBookBabDetailActivity.EXTRA_DATA, model)
                    intent.putExtra(MyBookBabDetailActivity.BAB_LIST, babList)
                    intent.putExtra(MyBookBabDetailActivity.NOVEL_ID, novelId)
                    intent.putExtra(MyBookBabDetailActivity.WRITER_ID, writerUid)
                    itemView.context.startActivity(intent)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBabBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(babList[position])
    }

    override fun getItemCount(): Int = babList.size
}