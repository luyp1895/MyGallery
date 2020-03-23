package com.example.gallery

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.gallery_cell.view.*

class GalleryAdapter: ListAdapter<PhotoItem,GalleryViewHolder>(DIFFCALLBACK){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.gallery_cell,parent,false)
        val holder = GalleryViewHolder(view)
        holder.itemView.setOnClickListener {
            Bundle().apply {
                putParcelableArrayList("photolist",ArrayList(currentList))
                putInt("currentNumber",holder.adapterPosition)
                holder.itemView.findNavController().navigate(R.id.action_galleryFragment_to_photoFragment,this)
            }
        }

        return holder

    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        holder.itemView.shimmerLayout_gallery.apply {
            setShimmerColor(0x55FFFFFF)
            setShimmerAngle(20)
            startShimmerAnimation()
        }
        Glide.with(holder.itemView).
            load(getItem(position).previewUrl)
            .placeholder(R.drawable.ic_image_gray_24dp)
            .listener(object :RequestListener<Drawable>{
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
                    return false.also { holder.itemView.shimmerLayout_gallery?.stopShimmerAnimation() }
                }

            })
            .into(holder.itemView.imageView_cell)
    }


    object DIFFCALLBACK:DiffUtil.ItemCallback<PhotoItem>() {
        override fun areItemsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
            return oldItem == newItem
        }
    }
}

class GalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)