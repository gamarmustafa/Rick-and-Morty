package com.example.rickandmorty.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.FragmentDetailsBinding
import com.squareup.picasso.Picasso

class DetailsFragment : Fragment(R.layout.fragment_details) {

    private val args by navArgs<DetailsFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentDetailsBinding.bind(view)
        binding.apply {
            val character = args.character
            Picasso.get().load(character.image).into(ivAvatar)
            tvName.text = character.name
            tvGender.text = character.gender
            tvStatus.text = character.status
            tvSpecies.text = character.species
            tvOrigin.text = character.origin.name
            if(character.type.isBlank()){
                tvType.text = "No data"
            }else{
                tvType.text = character.type
            }
        }
    }
}