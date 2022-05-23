package com.nnss.dev.cftest.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nnss.dev.cftest.data.local.model.CalcInput
import com.nnss.dev.cftest.databinding.ItemCalcHistoryBinding

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    private val list = mutableListOf<CalcInput>()

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<CalcInput>?) {
        this.list.clear()
        items?.let {
            this.list.addAll(it)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding: ItemCalcHistoryBinding =
            ItemCalcHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }


    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class HistoryViewHolder(
        private val view: ItemCalcHistoryBinding
    ) : RecyclerView.ViewHolder(view.root) {

        @SuppressLint("CheckResult")
        fun bind(item: CalcInput) {
            view.textInput.text = item.inputs
        }
    }

}



