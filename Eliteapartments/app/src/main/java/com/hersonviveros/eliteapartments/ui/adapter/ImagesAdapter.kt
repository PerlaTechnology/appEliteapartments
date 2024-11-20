package com.hersonviveros.eliteapartments.ui.adapter

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hersonviveros.eliteapartments.R
import com.hersonviveros.eliteapartments.utils.ItemTouchHelperAdapter


class ImagesAdapter : RecyclerView.Adapter<ImagesAdapter.ViewHolders>(), ItemTouchHelperAdapter {

    private val photos = mutableListOf<Uri>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newPhotos: List<Uri>) {
        photos.clear()
        photos.addAll(newPhotos)
        notifyDataSetChanged()
    }

    // Implementar método de arrastrar y soltar
    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        val movedPhoto = photos.removeAt(fromPosition)
        photos.add(toPosition, movedPhoto)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_images, parent, false)
        return ViewHolders(view)
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    override fun onBindViewHolder(holder: ViewHolders, position: Int) {
        Glide.with(holder.itemView)
            .load(photos[position])
            .placeholder(R.drawable.placeholder)
            .into(holder.imageView)
    }

    class ViewHolders(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.img_properties)
    }
}

