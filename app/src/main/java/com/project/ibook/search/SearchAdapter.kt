package com.project.ibook.search

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.ibook.books.other.novel_list.NovelDetailActivity
import com.project.ibook.books.other.anda_mungkin_suka.NovelModel5
import com.project.ibook.databinding.ItemNovelVerticalBinding

class SearchAdapter(private val option: String) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private val novelList = ArrayList<NovelModel5>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(items: ArrayList<NovelModel5>) {
        novelList.clear()
        novelList.addAll(items)
        notifyDataSetChanged()
    }


    inner class ViewHolder(private val binding: ItemNovelVerticalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(model: NovelModel5) {
            with(binding) {

                title.text = model.title
                description.text = model.synopsis
                Glide.with(itemView.context)
                    .load(model.image)
                    .into(image)

                cv.setOnClickListener {
                    val intent = Intent(itemView.context, NovelDetailActivity::class.java)
                    intent.putExtra(NovelDetailActivity.EXTRA_DATA, model)
                    itemView.context.startActivity(intent)
                }

                if (option == "deepLink") {
                    val intent = Intent(itemView.context, NovelDetailActivity::class.java)
                    intent.putExtra(NovelDetailActivity.EXTRA_DATA, model)
                    itemView.context.startActivity(intent)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemNovelVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(novelList[position])
    }

    override fun getItemCount(): Int = novelList.size
}