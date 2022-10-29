package com.example.rickandmorty.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.databinding.ItemCharacterBinding
import com.squareup.picasso.Picasso

class CharacterAdapter(private val listener: OnItemClickListener) :
    PagingDataAdapter<com.example.rickandmorty.data.Character, CharacterAdapter.CharacterViewHolder>(
        CHARACTER_COMPARATOR
    ) {

    inner class CharacterViewHolder(private val binding: ItemCharacterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                val item = getItem(position)
                if (item != null) {
                    listener.onItemClick(item)
                }
            }
        }

        fun bind(currentItem: com.example.rickandmorty.data.Character) {
            binding.apply {
                tvName.text = currentItem.name
                tvStatus.text = currentItem.status
                tvSpecies.text = currentItem.species
                tvGender.text = currentItem.gender
                Picasso.get().load(currentItem.image).into(ivAvatar)

            }
        }
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding =
            ItemCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharacterViewHolder(binding)
    }

    interface OnItemClickListener {
        fun onItemClick(character: com.example.rickandmorty.data.Character)
    }

    companion object {
        private val CHARACTER_COMPARATOR =
            object : DiffUtil.ItemCallback<com.example.rickandmorty.data.Character>() {
                override fun areItemsTheSame(
                    oldItem: com.example.rickandmorty.data.Character,
                    newItem: com.example.rickandmorty.data.Character
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: com.example.rickandmorty.data.Character,
                    newItem: com.example.rickandmorty.data.Character
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }

}