package com.project.ibook.books.other

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.ibook.books.my_book.MyBookBabModel
import com.project.ibook.databinding.ItemBabBinding

class NovelReadAdapter(
    private var babList: ArrayList<MyBookBabModel>,
) : RecyclerView.Adapter<NovelReadAdapter.ViewHolder>() {


    inner class ViewHolder(private val binding: ItemBabBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(model: MyBookBabModel) {
            with(binding) {

                titleBab.text = model.title
                descBab.text = model.description

                cv.setOnClickListener {
                    val intent = Intent(itemView.context, NovelReadActivity::class.java)
                    intent.putExtra(NovelReadActivity.EXTRA_DATA, model)
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