package com.example.gallery

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.photo_full_cell.view.*

class ViewPagerAdapter:ListAdapter<PhotoItem,FullPagerViewHolder>(DIFFCALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FullPagerViewHolder {
        LayoutInflater.from(parent.context).inflate(R.layout.photo_full_cell,parent,false).apply {
            return FullPagerViewHolder(this)
        }
    }

    override fun onBindViewHolder(holder: FullPagerViewHolder, position: Int) {
        holder.itemView.shimmerLayout_full.apply {
            setShimmerColor(0x55FFFFFF)
            setShimmerAngle(20)
            startShimmerAnimation()
        }

        Glide.with(holder.itemView)
            .load(getItem(position).fullUrl)
            .listener(object : RequestListener<Drawable>{
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false.also { holder.itemView.shimmerLayout_full.stopShimmerAnimation() }
                }

            })
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