package com.nnss.dev.cftest.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nnss.dev.cftest.data.remote.model.Comments
import com.nnss.dev.cftest.databinding.ItemCommentBinding

class CommentsAdapter : RecyclerView.Adapter<CommentsAdapter.HistoryViewHolder>() {
    private val list = mutableListOf<Comments>()

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<Comments>?) {
        this.list.clear()
        items?.let {
            this.list.addAll(it)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding: ItemCommentBinding =
            ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }


    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class HistoryViewHolder(
        val view: ItemCommentBinding
    ) : RecyclerView.ViewHolder(view.root) {

        @SuppressLint("CheckResult", "SetTextI18n")
        fun bind(item: Comments) {
            view.textUser.text = item.username
            view.textComment.text = item.text
            view.textCommentDate.text = item.createdAt
        }

    }

}



