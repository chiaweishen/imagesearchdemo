package com.scw.imagesearchdemo.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.scw.imagesearchdemo.databinding.ListRecentItemBinding

class RecentAdapter : RecyclerView.Adapter<RecentAdapter.RecentVieHolder>() {

    private val data = arrayListOf<String>()
    private var listener: AdapterListener? = null

    fun setData(data: List<String>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun setListener(listener: AdapterListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentVieHolder {
        return RecentVieHolder.newInstance(parent, listener)
    }

    override fun onBindViewHolder(holder: RecentVieHolder, position: Int) {
        holder.bindData(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class RecentVieHolder(
        private val binding: ListRecentItemBinding,
        private val listener: AdapterListener?
    ) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun newInstance(parent: ViewGroup, listener: AdapterListener?): RecentVieHolder {
                val binding = ListRecentItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                return RecentVieHolder(binding, listener)
            }
        }

        fun bindData(text: String) {
            binding.labelRecent.text = text
            binding.root.setOnClickListener { listener?.onItemClickListener(text) }
        }
    }

    interface AdapterListener {
        fun onItemClickListener(text: String)
    }
}