package com.project.ibook.books.other.novel_list

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.ibook.books.other.anda_mungkin_suka.NovelModel5
import com.project.ibook.books.other.cinta_abadi.NovelModel4
import com.project.ibook.books.other.pilihan_iBook.NovelModel2
import com.project.ibook.books.other.terlaris.NovelModel1
import com.project.ibook.books.other.wajib_dibaca.NovelModel3
import com.project.ibook.databinding.ItemNovelVerticalBinding

class NovelListAdapter(
    private val novelList1: ArrayList<NovelModel1>,
    private val novelList2: ArrayList<NovelModel2>,
    private val novelList3: ArrayList<NovelModel3>,
    private val novelList4: ArrayList<NovelModel4>,
    private val novelList5: ArrayList<NovelModel5>,
    private val option: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    inner class ViewHolderVertical(private val binding: ItemNovelVerticalBinding) :
        RecyclerView.ViewHolder(binding.root) {
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
                    intent.putExtra(NovelDetailActivity.OPTION, "5")
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    inner class ViewHolderHorizontal(private val binding: ItemNovelVerticalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(model: NovelModel2) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(model.image)
                    .into(image)

                title.text = model.title
                cv.setOnClickListener {
                    val intent = Intent(itemView.context, NovelDetailActivity::class.java)
                    intent.putExtra(NovelDetailActivity.EXTRA_DATA, model)
                    intent.putExtra(NovelDetailActivity.OPTION, "2")
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    inner class ViewHolderHorizontal3(private val binding: ItemNovelVerticalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(model: NovelModel3) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(model.image)
                    .into(image)

                title.text = model.title
                cv.setOnClickListener {
                    val intent = Intent(itemView.context, NovelDetailActivity::class.java)
                    intent.putExtra(NovelDetailActivity.EXTRA_DATA, model)
                    intent.putExtra(NovelDetailActivity.OPTION, "3")
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    inner class ViewHolderHorizontal4(private val binding: ItemNovelVerticalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(model: NovelModel4) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(model.image)
                    .into(image)

                title.text = model.title
                cv.setOnClickListener {
                    val intent = Intent(itemView.context, NovelDetailActivity::class.java)
                    intent.putExtra(NovelDetailActivity.EXTRA_DATA, model)
                    intent.putExtra(NovelDetailActivity.OPTION, "4")
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    inner class ViewHolderGrid(private val binding: ItemNovelVerticalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(model: NovelModel1) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(model.image)
                    .into(image)

                title.text = model.title
                writter.text = model.writerName
                cv.setOnClickListener {
                    val intent = Intent(itemView.context, NovelDetailActivity::class.java)
                    intent.putExtra(NovelDetailActivity.EXTRA_DATA, model)
                    intent.putExtra(NovelDetailActivity.OPTION, "1")
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (option) {
            "1" -> {
                val binding =
                    ItemNovelVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ViewHolderGrid(binding)
            }
            "2" -> {
                val binding = ItemNovelVerticalBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ViewHolderHorizontal(binding)
            }
            "3" -> {
                val binding = ItemNovelVerticalBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ViewHolderHorizontal3(binding)
            }
            "4" -> {
                val binding = ItemNovelVerticalBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ViewHolderHorizontal4(binding)
            }
            else -> {
                val binding = ItemNovelVerticalBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ViewHolderVertical(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (option) {
            "1" -> {
                (holder as ViewHolderGrid).bind(novelList1[position])
            }
            "2" -> {
                (holder as ViewHolderHorizontal).bind(novelList2[position])
            }
            "3" -> {
                (holder as ViewHolderHorizontal3).bind(novelList3[position])
            }
            "4" -> {
                (holder as ViewHolderHorizontal4).bind(novelList4[position])
            }
            else -> {
                (holder as ViewHolderVertical).bind(novelList5[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return when (option) {
            "1" -> {
                novelList1.size
            }
            "2" -> {
                novelList2.size
            }
            "3" -> {
                novelList3.size
            }
            "4" -> {
                novelList4.size
            }
            else -> {
                novelList5.size
            }
        }
    }
}