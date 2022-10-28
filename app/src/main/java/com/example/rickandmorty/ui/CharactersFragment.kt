package com.example.rickandmorty.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.rickandmorty.R
import com.example.rickandmorty.data.Character
import com.example.rickandmorty.databinding.FragmentCharactersBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CharactersFragment : Fragment(R.layout.fragment_characters),CharacterAdapter.OnItemClickListener {

    private var binding: FragmentCharactersBinding? = null
    private val viewModel by viewModels<CharactersViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCharactersBinding.bind(view)

        val adapter =CharacterAdapter(this)
        binding?.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(header = CharacterLoadStateAdapter{adapter.retry()}, footer =CharacterLoadStateAdapter{adapter.retry()} )
        }
        viewModel.characters.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle,it)
        }
    }

    override fun onItemClick(character: Character) {
        val action = CharactersFragmentDirections.actionCharactersFragmentToDetailsFragment(character)
        findNavController().navigate(action)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}