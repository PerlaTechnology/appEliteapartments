package com.hersonviveros.eliteapartments.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hersonviveros.eliteapartments.R
import com.hersonviveros.eliteapartments.data.database.entities.PropertyEntity
import com.hersonviveros.eliteapartments.databinding.ItemPropertyBinding
import com.hersonviveros.eliteapartments.utils.ItemTouchHelperAdapter


class PropertyAdapter(
    private val callback: ((data: PropertyEntity) -> Unit)? = null
) : RecyclerView.Adapter<PropertyAdapter.ViewHolders>(),
    ItemTouchHelperAdapter {

    private val entity = mutableListOf<PropertyEntity>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newEntity: List<PropertyEntity>) {
        entity.clear()
        entity.addAll(newEntity)
        notifyDataSetChanged()
    }

    // Implementar método de arrastrar y soltar
    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        val movedPhoto = entity.removeAt(fromPosition)
        entity.add(toPosition, movedPhoto)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders {
        val binding =
            ItemPropertyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolders(binding)
    }

    override fun getItemCount(): Int {
        return entity.size
    }

    override fun onBindViewHolder(holder: ViewHolders, position: Int) {
        holder.bind(entity[position])
    }

    inner class ViewHolders(private val binding: ItemPropertyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(entity: PropertyEntity) {
            Glide.with(binding.root)
                .load(entity.photos[0])
                .placeholder(R.drawable.placeholder)
                .into(binding.imgProperties)

            binding.tvTitle.text = entity.title
            binding.tvSubTitle.text = entity.description

            binding.relative.setOnClickListener {
                callback!!.invoke(entity)
            }
        }
    }
}