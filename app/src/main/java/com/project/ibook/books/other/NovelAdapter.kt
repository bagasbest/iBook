package com.project.ibook.books.other

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.ibook.databinding.ItemNovelGridBinding
import com.project.ibook.databinding.ItemNovelHorizontalBinding
import com.project.ibook.databinding.ItemNovelVerticalBinding

class NovelAdapter(private val novelList: ArrayList<NovelModel5>, private val option: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    inner class ViewHolderVertical(private val binding: ItemNovelVerticalBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(model5: NovelModel5) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(model5.image)
                    .into(image)

                title.text = model5.title
                writter.text = model5.writerName
                description.text = model5.synopsis
                cv.setOnClickListener {
                    val intent = Intent(itemView.context, NovelDetailActivity::class.java)
                    intent.putExtra(NovelDetailActivity.EXTRA_DATA, model5)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    inner class ViewHolderHorizontal(private val binding: ItemNovelHorizontalBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(model5: NovelModel5) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(model5.image)
                    .into(image)

                title.text = model5.title
                cv.setOnClickListener {
                    val intent = Intent(itemView.context, NovelDetailActivity::class.java)
                    intent.putExtra(NovelDetailActivity.EXTRA_DATA, model5)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    inner class ViewHolderGrid(private val binding: ItemNovelGridBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(model5: NovelModel5) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(model5.image)
                    .into(imageView)

                title.text = model5.title
                writer.text = model5.writerName
                cv.setOnClickListener {
                    val intent = Intent(itemView.context, NovelDetailActivity::class.java)
                    intent.putExtra(NovelDetailActivity.EXTRA_DATA, model5)
                    intent.putExtra(NovelDetailActivity.OPTION, "5")
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(option == "1") {
            val binding = ItemNovelGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
             ViewHolderGrid(binding)
        } else if (option == "2" || option == "3" || option == "4") {
            val binding = ItemNovelHorizontalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ViewHolderHorizontal(binding)
        }
        else {
            val binding = ItemNovelVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ViewHolderVertical(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(option == "1") {
            (holder as ViewHolderGrid).bind(novelList[position])
        }
        else if(option == "2" || option == "3" || option == "4") {
            (holder as ViewHolderHorizontal).bind(novelList[position])
        } else {
            (holder as ViewHolderVertical).bind(novelList[position])
        }
    }

    override fun getItemCount(): Int = novelList.size
}