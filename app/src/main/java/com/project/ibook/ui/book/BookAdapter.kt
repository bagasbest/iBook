package com.project.ibook.ui.book

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.ibook.books.other.novel_list.NovelDetailActivity
import com.project.ibook.books.other.terlaris.NovelModel1
import com.project.ibook.databinding.ItemBookGridBinding

class BookAdapter(
    private var novelList: ArrayList<NovelModel1>,
) : RecyclerView.Adapter<BookAdapter.ViewHolder>() {


    inner class ViewHolder(private val binding: ItemBookGridBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(model: NovelModel1, position: Int) {
            with(binding) {

                Glide.with(itemView.context)
                    .load(model.image)
                    .into(binding.image)

                title.text = model.title

                cv.setOnClickListener {
                    val intent = Intent(itemView.context, NovelDetailActivity::class.java)
                    intent.putExtra(NovelDetailActivity.EXTRA_DATA, model)
                    intent.putExtra(NovelDetailActivity.OPTION, "1")
                    intent.putExtra(NovelDetailActivity.IN_RAK_BUKU, "yes")
                    itemView.context.startActivity(intent)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBookGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(novelList[position], position)
    }

    override fun getItemCount(): Int = novelList.size
}