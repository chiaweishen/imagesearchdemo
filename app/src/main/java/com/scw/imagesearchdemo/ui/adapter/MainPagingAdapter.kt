package com.scw.imagesearchdemo.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.scw.imagesearchdemo.databinding.ListImageItemBinding
import com.scw.imagesearchdemo.model.data.Image

class MainPagingAdapter : PagingDataAdapter<Image, MainPagingAdapter.MainViewHolder>(DiffCallback) {

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder.newInstance(parent)
    }

    object DiffCallback : DiffUtil.ItemCallback<Image>() {

        override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem == newItem
        }
    }

    class MainViewHolder(
        private val binding: ListImageItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun newInstance(parent: ViewGroup): MainViewHolder {
                val binding = ListImageItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                return MainViewHolder(binding)
            }
        }

        fun bindData(image: Image?) {
            binding.image.load(image?.url)
        }
    }
}