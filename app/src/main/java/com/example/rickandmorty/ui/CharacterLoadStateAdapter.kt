package com.example.rickandmorty.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.databinding.LoadStateHeaderFooterBinding
import kotlin.math.log

class CharacterLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<CharacterLoadStateAdapter.LoadStateViewHolder>() {

    //inner  so we can use retry parameter
    inner class LoadStateViewHolder(private val binding: LoadStateHeaderFooterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btRetry.setOnClickListener {
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState) {
            binding.apply {
                progressBar.isVisible = loadState is LoadState.Loading
                btRetry.isVisible = loadState !is LoadState.Loading
                tvError.isVisible = loadState !is LoadState.Loading

                //hide retry button when end of pagination reached
                if (loadState is LoadState.Error) {
                    //Log.i("loadState.Error message", loadState.error?.localizedMessage.toString())
                    val errorMessage =loadState.error?.localizedMessage.toString()
                    if(errorMessage.trim() == "HTTP 404"){
                        btRetry.isVisible =false
                        tvError.isVisible =false
                    }
                }
            }
        }
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding =
            LoadStateHeaderFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadStateViewHolder(binding)
    }
}