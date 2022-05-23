package com.nnss.dev.cftest.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nnss.dev.cftest.data.remote.model.FeedResponse
import com.nnss.dev.cftest.databinding.ItemPostsBinding

class PostAdapter(val context: Context, val listener: IClickListener) : RecyclerView.Adapter<PostAdapter.HistoryViewHolder>() {
    private val list = mutableListOf<FeedResponse>()

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<FeedResponse>?) {
        this.list.clear()
        items?.let {
            this.list.addAll(it)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding: ItemPostsBinding =
            ItemPostsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }


    override fun getItemCount(): Int = list.size

    interface IClickListener {
        fun onItemClick(item: FeedResponse)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(list[position])

        if (position == itemCount - 1) {
            holder.view.listLine.visibility = View.GONE
        } else {
            holder.view.listLine.visibility = View.VISIBLE
        }
    }

    inner class HistoryViewHolder(
        val view: ItemPostsBinding
    ) : RecyclerView.ViewHolder(view.root) {

        @SuppressLint("CheckResult", "SetTextI18n")
        fun bind(item: FeedResponse) {
            Glide.with(context).load(item.user?.profilePic).into(view.imgProfilePic)
            view.textName.text = item.user?.firstName + item.user?.lastName
            view.textUser.text = item.username
            view.textComment.text = item.text
            view.itemContainer.setOnClickListener {
                listener.onItemClick(item)
            }

        }

    }

}



