package com.project.ibook.books.other.novel_list.comment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.ibook.databinding.ItemCommentBinding

class CommentAdapter: RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    private val commentList = ArrayList<CommentModel>()
    @SuppressLint("NotifyDataSetChanged")
    fun setData(items: ArrayList<CommentModel>) {
        commentList.clear()
        commentList.addAll(items)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(model: CommentModel, position: Int) {
            with(binding) {

                name.text = model.username
                date.text = model.date
                comment.text = model.comment

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(commentList[position], position)
    }

    override fun getItemCount(): Int = commentList.size
}