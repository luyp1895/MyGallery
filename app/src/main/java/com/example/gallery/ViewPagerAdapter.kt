package com.example.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.photo_full_cell.view.*

class ViewPagerAdapter:ListAdapter<PhotoItem,FullPagerViewHolder>(DIFFCALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FullPagerViewHolder {
        LayoutInflater.from(parent.context).inflate(R.layout.photo_full_cell,parent,false).apply {
            return FullPagerViewHolder(this)
        }
    }

    override fun onBindViewHolder(holder: FullPagerViewHolder, position: Int) {

        Glide.with(holder.itemView)
            .load(getItem(position).fullUrl)
            .placeholder(R.drawable.ic_image_gray_24dp)
            .into(holder.itemView.photoView)
    }


    object DIFFCALLBACK : DiffUtil.ItemCallback<PhotoItem>() {
        override fun areItemsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
            return oldItem == newItem
        }
    }
}

class FullPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)